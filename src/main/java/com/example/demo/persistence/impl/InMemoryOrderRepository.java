package com.example.demo.persistence.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {

  private static final Logger logger = Logger.getLogger(InMemoryOrderRepository.class.getName());
  private final Map<String, Order> database = new HashMap<>();

  @Override
  public void save(Order order) {
    if (order == null || order.getOrderId() == null) {
      return;
    }
    database.put(order.getOrderId(), order);
    logger.info(() -> "Order saved with id: " + order.getOrderId());
  }

  @Override
  public void update(Order order) {
    
  }

  @Override
  public void delete(String id) {
    
  }

  @Override
  public Order get(String id) {
    return null;
  }

  @Override
  public List<Order> filter() {
    return null;
  }
}
