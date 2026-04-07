package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;

public class OrderServiceImpl implements OrderService {

  private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  private static int counter = 1;

  private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

  private static String generateOrderId() {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
    return String.format("ORD-%s-%05d", date, counter++);
  }

  private void validateCreateRequest(CreateOrderRequest request) {
    if (request == null) {
      throw new ValidationException("Request cannot be null.");
    }
    if (request.getCustomerId() == null || request.getCustomerId() <= 0) {
      throw new ValidationException("CustomerId cannot be null and must be > 0.");
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

  @Override
  public Order create(CreateOrderRequest request) {
    logger.info(() -> "Creating order with customer name: " + request.getCustomerName());
    validateCreateRequest(request);
    LocalDateTime now = LocalDateTime.now();
    Order order = Order.builder()
        .orderId(generateOrderId())
        .customerId(request.getCustomerId())
        .customerName(request.getCustomerName())
        .amount(request.getAmount())
        .paymentMethod(request.getPaymentMethod())
        .status(OrderStatus.PENDING)
        .createdAt(now)
        .updatedAt(now)
        .build();

    orderRepository.save(order);
    return order;
  }

  @Override
  public Order update(Order order) {
    logger.info(() -> "Updating order with id " + order.getOrderId());

    orderRepository.update(order);

    return order;
  }

  @Override
  public Order get(String orderId) {
    logger.info(() -> "Getting order with id: " + orderId);

    if (orderId == null || orderId.trim().isEmpty()) {
      throw new ValidationException("orderId cannot be null or empty.");
    }
    Order order = orderRepository.findById(orderId);
    if (order == null) {
      throw new OrderNotFoundException(orderId);
    }

    return order;
  }

  @Override
  public void delete(String orderId) {

  }

  @Override
  public Order cancelOrder(String orderId, String cancelReason) {
    logger.info(() -> "cancelOrder param: orderId=" + orderId + ", reason=" + cancelReason);

    if (orderId == null || orderId.isBlank()) {
      throw new ValidationException("orderId must not be empty");
    }
    if (cancelReason == null || cancelReason.isBlank()) {
      throw new ValidationException("cancelReason must not be empty");
    }
    Order order = get(orderId);
    order.cancel(cancelReason);
    orderRepository.update(order);

    return order;
  }

  private void validateFilterRequest(OrderFilterRequest request) {
    if (request.getFromDate() != null && request.getToDate() != null
        && request.getFromDate().isAfter(request.getToDate())) {
      throw new ValidationException("fromDate must be before or equal to toDate.");
    }
  }

  @Override
  public List<Order> findAll(OrderFilterRequest request) {
    final OrderFilterRequest actualRequest = request != null ? request : new OrderFilterRequest();

    logger.info(() -> "findAll param: " + actualRequest);

    validateFilterRequest(actualRequest);
    return orderRepository.findAll(actualRequest);
  }

}
