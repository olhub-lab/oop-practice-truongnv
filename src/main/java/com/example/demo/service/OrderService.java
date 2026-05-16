package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;

public interface OrderService {
  OrderResponse create(CreateOrderRequest request);

  OrderResponse get(String orderId);

  List<OrderResponse> findAll(OrderFilterRequest request);

  CancelOrderResponse cancelOrder(String orderId, String reason);

  PaymentOrderResponse processPayment(String orderId);
}
