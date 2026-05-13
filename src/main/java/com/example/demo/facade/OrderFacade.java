package com.example.demo.facade;

import java.util.List;

import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;

public interface OrderFacade {
  OrderResponse createOrder(CreateOrderRequest request);

  OrderResponse getOrder(String orderId);

  List<OrderResponse> filterOrders(String status, String paymentMethod, String fromDate, String toDate);

  CancelOrderResponse cancelOrder(String orderId, String reason);

  PaymentOrderResponse processPayment(String orderId);
}
