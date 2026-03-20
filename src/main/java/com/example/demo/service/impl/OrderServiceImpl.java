package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.dto.response.OrderDetailResponse;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.persistence.OrderDAO;
import com.example.demo.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class OrderServiceImpl implements OrderService {

  private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
  private static final AtomicLong counter = new AtomicLong(0);

  private final OrderDAO orderDAO;

  public OrderServiceImpl(OrderDAO orderDAO) {
    this.orderDAO = orderDAO;
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
    if (request.getPaymentMethod() == null) {
      throw new ValidationException("PaymentMethod cannot be null.");
    }
    if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Amount must be greater than 0.");
    }
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

    orderDAO.save(newOrder);

    logger.info("Created oder successfully" + newOrder.getOrderId());

    return newOrder;
  }

  @Override
  public OrderDetailResponse getOrderDetail(String orderId) {
    logger.info("Getting order detail with id " + orderId);

    if(orderId == null || orderId.trim().isEmpty()) {
      throw new ValidationException("OrderId cannot be null.");
    }
    Order order = orderDAO.findById(orderId);

    if(order == null) {
      throw new OrderNotFoundException("Order with id " + orderId + " not found.");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    return OrderDetailResponse.builder().orderId(order.getOrderId()).customerId(order.getCustomerId()).customerName(order.getCustomerName()).amount(order.getAmount()).paymentMethod(order.getPaymentMethod().name()).feeAmount(order.getFeeAmount()).discountAmount(order.getDiscountAmount()).finalAmount(order.getFinalAmount()).status(order.getStatus().name()).createdAt(order.getCreatedAt().format(formatter)).updatedAt(order.getUpdatedAt().format(formatter)).build();
  }
}
