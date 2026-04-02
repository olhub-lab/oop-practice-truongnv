package com.example.demo.persistence.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {

  private static final Logger logger = Logger.getLogger(InMemoryOrderRepository.class.getName());
  private final Map<String, Order> database = new HashMap<>();

  @Override
  public void save(Order order) {
    if (order == null || order.getOrderId() == null) {
      logger.warning(() -> "Attempted to save null order or order without id");
      return;
    }
    logger.info(() -> "Saving order with id: " + order.getOrderId());
    database.put(order.getOrderId(), order);
  }

  @Override
  public void update(Order order) {
  }

  @Override
  public void delete(String id) {

  }

  @Override
  public Order findById(String id) {
    logger.info(() -> "get order with id: " + id);
    return database.get(id);
  }

  @Override
  public List<Order> findAll(OrderFilterRequest request) {
    logger.info(() -> "Finding all orders with filter " + request);

    if (request == null) {
      return database.values().stream()
          .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
          .collect(Collectors.toList());
    }

    return database.values().stream()
        .filter(order -> {
          if (request.getStatus() != null
              && !request.getStatus().equals(order.getStatus())) {
            return false;
          }
          if (request.getPaymentMethod() != null
              && !request.getPaymentMethod().equals(order.getPaymentMethod())) {
            return false;
          }
          if (request.getFromDate() != null
              && order.getCreatedAt().toLocalDate().isBefore(request.getFromDate())) {
            return false;
          }
          if (request.getToDate() != null
              && order.getCreatedAt().toLocalDate().isAfter(request.getToDate())) {
            return false;
          }
          return true;
        })
        .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

}
