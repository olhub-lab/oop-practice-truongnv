package com.example.demo.persistence.impl;

import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class InMemoryOrderRepository implements OrderRepository {

  private static final Logger logger = Logger.getLogger(InMemoryOrderRepository.class.getName());
  private final Map<String, Order>database = new HashMap<>();

  @Override
  public void save(Order order) {
    database.put(order.getOrderId(), order);
    logger.info(order.getOrderId());
  }

  @Override
  public Order findById(String orderId) {
    return database.get(orderId);
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(database.values());
  }
}
