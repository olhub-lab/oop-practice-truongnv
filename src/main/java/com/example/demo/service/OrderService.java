package com.example.demo.service;

import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.dto.response.OrderDetailResponse;
import com.example.demo.model.Order;

public interface OrderService {
  Order createOrder(CreateOrderRequest request);

  OrderDetailResponse getOrderDetail(String orderId);
}
