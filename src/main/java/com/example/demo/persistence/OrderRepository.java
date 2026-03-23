package com.example.demo.persistence;

import com.example.demo.model.Order;
import java.util.List;

public interface OrderRepository {
  void save(Order order);

  Order findById(String orderId);
  List<Order> findAll();
}
