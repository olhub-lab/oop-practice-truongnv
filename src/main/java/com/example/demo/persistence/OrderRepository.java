package com.example.demo.persistence;

import java.util.List;

import com.example.demo.model.Order;

public interface OrderRepository {
  void save(Order order);

  void update(Order order);

  Order get(String id);

  void delete(String id);

  List<Order> filter();

}
