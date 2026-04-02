package com.example.demo.facade.impl;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.order.UpdateOrderRequest;
import com.example.demo.facade.OrderFacade;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

public class OrderFacadeImpl implements OrderFacade {
  private static final Logger logger = Logger.getLogger(OrderFacadeImpl.class.getName());

  private final OrderService orderService;

  public OrderFacadeImpl(OrderService orderService) {
    this.orderService = orderService;
  }

  @Override
  public OrderResponse createOrder(CreateOrderRequest request) {
    logger.info(() -> "Creating order for customer: " + request.getCustomerName());

    Order createdOrder = orderService.create(request);

    return new OrderResponse(createdOrder);
  }

  @Override
  public OrderResponse updateOrder(String orderId, UpdateOrderRequest request) {
    logger.info(() -> "Updating order with id " + orderId);

    return null;
  }

  @Override
  public OrderResponse getOrder(String orderId) {
    logger.info(() -> "Getting order with id " + orderId);

    Order order = orderService.get(orderId);
    return new OrderResponse(order);
  }

  @Override
  public void deleteOrder(String orderId) {
    logger.info(() -> "Deleting order with id " + orderId);

  }

  @Override
  public List<OrderResponse> filterOrders(OrderFilterRequest request) {
    logger.info(() -> "Filtering orders param: " + request);
    return orderService.findAll(request).stream()
        .map(OrderResponse::new)
        .collect(Collectors.toList());
  }
}