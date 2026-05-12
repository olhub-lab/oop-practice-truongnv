package com.example.demo.persistence.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryOrderRepository implements OrderRepository {

  private final Map<String, Order> database = new HashMap<>();
  private final AtomicLong orderCounter = new AtomicLong();

  @Override
  public long nextOrderSequence() {
    log.info("get next order sequence");
    return orderCounter.incrementAndGet();
  }

  @Override
  public void save(Order order) {
    if (order == null || order.getOrderId() == null) {
      log.warn("Attempted to save null order or order without id");
      return;
    }
    log.info("Saving order with id: {}", order.getOrderId());
    database.put(order.getOrderId(), order);
  }

  @Override
  public void update(Order order) {
    if (order != null && order.getOrderId() != null) {
      log.info("Updating order in database with id: {}", order.getOrderId());
      database.put(order.getOrderId(), order);
    }
  }

  @Override
  public void delete(String id) {

  }

  @Override
  public Order findById(String id) {
    log.info("get order with id: {}", id);
    return database.get(id);
  }

  @Override
  public List<Order> findAll(OrderFilterRequest request) {
    log.info("Finding all orders with filter {}", request);

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
              && order.getCreatedAt().isBefore(request.getFromDate())) {
            return false;
          }
          if (request.getToDate() != null
              && order.getCreatedAt().isAfter(request.getToDate())) {
            return false;
          }
          return true;
        })
        .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

}
