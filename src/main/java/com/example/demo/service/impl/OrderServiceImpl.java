package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.payment.PaymentPort;
import com.example.demo.payment.PaymentPortResolver;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderRepository orderRepository;
  private final PaymentPortResolver paymentPortResolver;

  public OrderServiceImpl(OrderRepository orderRepository, PaymentPortResolver paymentPortResolver) {
    this.orderRepository = orderRepository;
    this.paymentPortResolver = paymentPortResolver;
  }

  private static String generateOrderId() {
    String date = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String shortId = UUID.randomUUID()
        .toString()
        .replace("-", "")
        .substring(0, 8)
        .toUpperCase();
    return String.format("ORD-%s-%s", date, shortId);
  }


  @Override
  @Transactional
  public Order create(CreateOrderRequest request) {
    logger.info("Creating order with customer name: {}", request.getCustomerName());
    
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
  @Transactional
  public Order update(Order order) {
    logger.info("Updating order with id {}", order.getOrderId());

    orderRepository.update(order);

    return order;
  }

  @Override
  public Order get(String orderId) {
    logger.info("Getting order with id: {}", orderId);

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
  @Transactional
  public void delete(String orderId) {
    logger.info("Deleting order with id {}", orderId);
    if (orderId == null || orderId.trim().isEmpty()) {
      throw new ValidationException("orderId cannot be null or empty.");
    }
    get(orderId);
    orderRepository.delete(orderId);
  }

  @Override
  @Transactional
  public Order cancelOrder(String orderId, String cancelReason) {
    logger.info("cancelOrder param: orderId={}, reason={}", orderId, cancelReason);

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
  @Transactional(readOnly = true)
  public List<Order> findAll(OrderFilterRequest request) {
    final OrderFilterRequest actualRequest = request != null ? request : new OrderFilterRequest();

    logger.info("findAll param: {}", actualRequest);

    validateFilterRequest(actualRequest);
    return orderRepository.findAll(actualRequest);
  }

  @Override
  @Transactional
  public Order processPayment(String orderId) {
    logger.info("processPayment param: orderId={}", orderId);

    Order order = this.get(orderId);
    order.validatePendingStatus();

    PaymentPort port = this.paymentPortResolver.getPaymentPort(order.getPaymentMethod());
    OrderStatus status = port.process(order);

    order.applyPaymentResult(status);
    this.orderRepository.update(order);

    return order;
  }

}
