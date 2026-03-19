package com.example.demo.persistence.impl;

import com.example.demo.model.Order;
import com.example.demo.persistence.OrderDAO;
import java.util.HashMap;
import java.util.Map;

public class InMemoryOrderDaoImpl implements OrderDAO {
  private final Map<String, Order>database = new HashMap<>();

  @Override
  public void save(Order order) {
    database.put(order.getOrderId(), order);
  }
}
