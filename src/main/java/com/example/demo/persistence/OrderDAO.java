package com.example.demo.persistence;

import com.example.demo.model.Order;

public interface OrderDAO {

  void save(Order order);

  Order findById(String orderId);
}
