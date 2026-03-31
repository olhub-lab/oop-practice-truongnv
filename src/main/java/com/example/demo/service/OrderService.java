package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;

public interface OrderService {
  Order create(CreateOrderRequest request);

  Order update(Order order);

  Order get(String id);

  void delete(String id);

  List<Order> findAll(OrderFilterRequest request);
}
