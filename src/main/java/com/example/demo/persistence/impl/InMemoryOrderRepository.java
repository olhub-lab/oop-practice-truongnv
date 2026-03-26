package com.example.demo.persistence.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {

  private static final Logger logger = Logger.getLogger(InMemoryOrderRepository.class.getName());
  private final Map<String, Order> database = new HashMap<>();

  @Override
  public void save(Order order) {
    database.put(order.getOrderId(), order);
    logger.info(() -> "save param: " + order.getOrderId());
  }
}
