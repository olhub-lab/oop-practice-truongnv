package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;

public interface OrderService {
  OrderResponse createOrder(CreateOrderRequest request);

  OrderResponse getOrder(String orderId);

  List<OrderResponse> findAll(String status, String paymentMethod, String fromDate, String toDate);

  CancelOrderResponse cancelOrder(String orderId, String reason);

  PaymentOrderResponse processPayment(String orderId);
}
