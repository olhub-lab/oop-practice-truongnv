package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Order;

public interface OrderService {
  Order create(Order order);

  Order update(Order order);

  Order get(String id);

  void delete(String id);

  List<Order> filter();

  Order cancelOrder(String orderId, String reason);
}
