package com.example.demo.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;

public interface OrderService {
  Order create(Order order);

  Order update(Order order);

  Order get(String id);

  void delete(String id);

  PageResponse<Order> findAll(OrderFilterRequest filter);
}
