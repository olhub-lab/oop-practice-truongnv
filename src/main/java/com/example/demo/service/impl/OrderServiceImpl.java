package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.persistence.OrderDAO;
import com.example.demo.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class OrderServiceImpl implements OrderService {

  private final OrderDAO orderDAO;

  public OrderServiceImpl(OrderDAO orderDAO) {
    this.orderDAO = orderDAO;
  }

  private void validateRequest(CreateOrderRequest request) {
    if (request == null) {
      throw new ValidationException("Request cannot be null.");
    }
    if (request.getCustomerId() == null) {
      throw new ValidationException("CustomerId cannot be null.");
    }
    if (request.getPaymentMethod() == null) {
      throw new ValidationException("PaymentMethod cannot be null.");
    }
    if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Amount must be greater than 0.");
    }
  }

  private static final AtomicLong counter = new AtomicLong(0);

  @Override
  public Order createOrder(CreateOrderRequest request) {
    validateRequest(request);

    LocalDateTime now = LocalDateTime.now();
    BigDecimal amount = request.getAmount();

    Order newOrder = Order.builder().orderId(String.valueOf(counter.incrementAndGet()))
        .customerId(request.getCustomerId()).customerName(request.getCustomerName()).amount(amount)
        .paymentMethod(request.getPaymentMethod()).feeAmount(BigDecimal.ZERO)
        .discountAmount(BigDecimal.ZERO).finalAmount(amount).status(
            OrderStatus.PENDING).createdAt(now).updatedAt(now).build();

    orderDAO.save(newOrder);
    return newOrder;
  }
}
