package com.example.demo.service;

import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.model.Order;

public interface OrderService {
  Order createOrder(CreateOrderRequest request);
}
