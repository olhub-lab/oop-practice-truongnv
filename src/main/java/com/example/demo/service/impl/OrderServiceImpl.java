package com.example.demo.service.impl;

import java.util.List;
import java.util.logging.Logger;

import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;

public class OrderServiceImpl implements OrderService {

  private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Order create(Order order) {
    logger.info(() -> "Creating order with id: " + order.getOrderId());
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
    return null;
  }

  @Override
  public void delete(String orderId) {

  }

  @Override
  public List<Order> filter() {
    return null;
  }

  @Override
  public Order cancelOrder(String orderId, String reason) {
    logger.info(() -> "cancelOrder param: orderId=" + orderId + ", reason=" + reason);
    Order order = orderRepository.get(orderId);

    if (order == null) {
      throw new OrderNotFoundException("Order not found with id: " + orderId);
    }

    order.cancel(reason);
    orderRepository.update(order);

    return order;
  }
}