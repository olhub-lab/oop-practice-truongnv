package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.dto.request.ListOrderRequest;
import com.example.demo.dto.response.CancelOrderResponse;
import com.example.demo.dto.response.OrderDetailResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;

public class OrderServiceImpl implements OrderService {

  private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
  private static final AtomicLong counter = new AtomicLong(0);

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  private String generateOrderId() {
    String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    return String.format("ORD-%s-%05d", datePart, counter.incrementAndGet());
  }

  private void validateRequest(CreateOrderRequest request) {
    if (request == null) {
      throw new ValidationException("Request cannot be null.");
    }
    if (request.getCustomerId() == null || request.getCustomerId() <= 0) {
      throw new ValidationException("CustomerId cannot be null.");
    }
    if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
      throw new ValidationException("CustomerName cannot be empty.");
    }
    if (request.getCustomerName().length() > 100) {
      throw new ValidationException("CustomerName cannot be longer than 100 characters.");
    }
    if (request.getPaymentMethod() == null) {
      throw new ValidationException("PaymentMethod cannot be null.");
    }
    if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Amount must be greater than 0.");
    }
  }

  private OrderDetailResponse mapToOrderDetailResponse(Order order) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    OrderDetailResponse response = new OrderDetailResponse();
    response.setOrderId(order.getOrderId());
    response.setCustomerId(order.getCustomerId());
    response.setCustomerName(order.getCustomerName());
    response.setAmount(order.getAmount());
    response.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null);
    response.setFeeAmount(order.getFeeAmount());
    response.setDiscountAmount(order.getDiscountAmount());
    response.setFinalAmount(order.getFinalAmount());
    response.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
    if (order.getCreatedAt() != null) {
      response.setCreatedAt(order.getCreatedAt().format(formatter));
    }
    if (order.getUpdatedAt() != null) {
      response.setUpdatedAt(order.getUpdatedAt().format(formatter));
    }
    response.setCancelReason(order.getCancelReason());

    return response;
  }

  @Override
  public Order createOrder(CreateOrderRequest request) {
    logger.info("Creating order with id " + request.getCustomerId());

    validateRequest(request);

    LocalDateTime now = LocalDateTime.now();

    Order newOrder = Order.builder()
        .orderId(generateOrderId())
        .customerId(request.getCustomerId())
        .customerName(request.getCustomerName())
        .amount(request.getAmount())
        .paymentMethod(request.getPaymentMethod())
        .status(OrderStatus.PENDING)
        .createdAt(now)
        .updatedAt(now)
        .build();

    newOrder.calculateFinalAmount();

    orderRepository.save(newOrder);

    logger.info("Created order successfully" + newOrder.getOrderId());

    return newOrder;
  }

  @Override
  public OrderDetailResponse getOrderDetail(String orderId) {
    logger.info("Getting order details for orderId " + orderId);

    if (orderId == null || orderId.trim().isEmpty()) {
      throw new ValidationException("OrderId cannot be null.");
    }

    Order order = orderRepository.findById(orderId);
    if (order == null) {
      throw new ValidationException("Order Not Found: " + orderId);
    }

    return mapToOrderDetailResponse(order);
  }

  @Override
  public CancelOrderResponse cancelOrder(String orderId, String reason) {
    logger.info("Canceling order with id " + orderId);
    if (orderId == null || orderId.trim().isEmpty()) {
      throw new ValidationException("OrderId cannot be null.");
    }

    Order order = orderRepository.findById(orderId);
    if (order == null) {
      throw new ValidationException("Order Not Found: " + orderId);
    }
    String oldStatus = order.getStatus().name();
    order.cancel(reason);

    orderRepository.save(order);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    CancelOrderResponse response = new CancelOrderResponse();
    response.setOrderId(order.getOrderId());
    response.setOldStatus(oldStatus);
    response.setNewStatus(order.getStatus().name());
    response.setCancelReason(order.getCancelReason());
    response.setCanceledAt(order.getUpdatedAt().format(formatter));
    response.setMessage("Order Cancel successfully");

    logger.info("Canceled order successfully" + orderId);

    return response;
  }

  @Override
  public PageResponse<OrderDetailResponse> listOrders(ListOrderRequest request) {
    int page = Math.max(request.getPage(), 0);
    int size = request.getSize() <= 0 ? 10 : Math.min(request.getSize(), 100);

    List<Order> allOrders = orderRepository.findAll();

    List<Order> filteredOrders = allOrders.stream()
        .filter(o -> request.getCustomerId() == null || o.getCustomerId().equals(request.getCustomerId()))
        .filter(o -> request.getStatus() == null || o.getStatus() == request.getStatus())
        .filter(o -> request.getPaymentMethod() == null || o.getPaymentMethod() == request.getPaymentMethod())
        .filter(o -> request.getFromDate() == null
            || (o.getCreatedAt() != null && !o.getCreatedAt().toLocalDate().isBefore(request.getFromDate())))
        .filter(o -> request.getToDate() == null
            || (o.getCreatedAt() != null && !o.getCreatedAt().toLocalDate().isAfter(request.getToDate())))
        .collect(Collectors.toList());

    Comparator<Order> comparator = Comparator.comparing(Order::getCreatedAt);
    if ("AMOUNT".equalsIgnoreCase(request.getSortBy())) {
      comparator = Comparator.comparing(Order::getFinalAmount);
    } else if ("NAME".equalsIgnoreCase(request.getSortBy())) {
      comparator = Comparator.comparing(Order::getCustomerName);
    }

    if (!"ASC".equalsIgnoreCase(request.getSortOrder())) {
      comparator = comparator.reversed();
    }

    filteredOrders.sort(comparator);

    int totalElements = filteredOrders.size();
    int totalPages = (int) Math.ceil((double) totalElements / size);

    int start = page * size;
    int end = Math.min(start + size, totalElements);

    List<OrderDetailResponse> content;
    if (start >= totalElements) {
      content = Collections.emptyList();
    } else {
      content = filteredOrders.subList(start, end).stream()
          .map(o -> this.mapToOrderDetailResponse(o))
          .collect(Collectors.toList());
    }

    PageResponse<OrderDetailResponse> response = new PageResponse<>();
    response.setContent(content);
    response.setTotalElements(totalElements);
    response.setTotalPages(totalPages);
    response.setHasNext(page < totalPages - 1);
    response.setHasPrevious(page > 0);

    return response;
  }
}
