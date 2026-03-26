package com.example.demo.facade.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.order.UpdateOrderRequest;
import com.example.demo.exception.ValidationException;
import com.example.demo.facade.OrderFacade;
import com.example.demo.service.OrderService;

public class OrderFacadeImpl implements OrderFacade {
  private static final Logger logger = Logger.getLogger(OrderFacadeImpl.class.getName());

  private final OrderService orderService;

  public OrderFacadeImpl(OrderService orderService) {
    this.orderService = orderService;
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
  public OrderResponse createOrder(CreateOrderRequest request) {
    return null;
  }

  @Override
  public OrderResponse updateOrder(String orderId, UpdateOrderRequest request) {
    logger.info(() -> "Updating order with id " + orderId);

    return null;
  }

  @Override
  public OrderResponse getOrder(String orderId) {
    logger.info(() -> "Getting order with id " + orderId);

    return null;
  }

  @Override
  public void deleteOrder(String orderId) {
    logger.info(() -> "Deleting order with id " + orderId);

  }

  @Override
  public List<OrderResponse> filterOrders(OrderFilterRequest request) {
    logger.info(() -> "Filtering orders with request " + request);

    return null;
  }

}
