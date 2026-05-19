package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;

public interface OrderService {
  Order create(CreateOrderRequest request);

  Order get(String orderId);

  List<Order> findAll(OrderFilterRequest request);

  Order cancelOrder(String orderId, String reason);

  void applyPaymentResult(String orderId, OrderStatus status);

}
