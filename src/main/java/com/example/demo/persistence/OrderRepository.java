package com.example.demo.persistence;

import java.util.List;

import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;

public interface OrderRepository {

  long nextOrderSequence();

  void save(Order order);

  void update(Order order);

  Order findById(String id);

  void delete(String id);

  List<Order> findAll(OrderFilterRequest request);

}
