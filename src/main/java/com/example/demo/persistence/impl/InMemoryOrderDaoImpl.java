package com.example.demo.persistence.impl;

import com.example.demo.model.Order;
import com.example.demo.persistence.OrderDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class InMemoryOrderDaoImpl implements OrderDAO {

  private static final Logger logger = Logger.getLogger(InMemoryOrderDaoImpl.class.getName());
  private final Map<String, Order>database = new HashMap<>();

  @Override
  public void save(Order order) {
    database.put(order.getOrderId(), order);
    logger.info(order.getOrderId());
  }
}
