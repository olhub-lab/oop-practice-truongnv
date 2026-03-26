package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

import com.example.demo.dto.order.CreateOrderRequest;
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

  @Override
  public Order createOrder(Order order) {
    logger.info(() -> "Service executing save order: " + order.getOrderId());

    orderRepository.save(order);
    return order;
  }


}
