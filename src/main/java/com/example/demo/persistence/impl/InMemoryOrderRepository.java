package com.example.demo.persistence.impl;

import java.util.ArrayList;
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
    database.put(order.getOrderId(), order);
  }

  @Override
  public void update(Order order) {
    database.put(order.getOrderId(), order);
  }

  @Override
  public void delete(String id) {
    database.remove(id);
  }

  @Override
  public Order get(String id) {
    return database.get(id);
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(database.values());
  }
}
