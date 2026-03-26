package com.example.demo.facade;


import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;

public interface OrderFacade {
  OrderResponse createOrder(CreateOrderRequest request);
}