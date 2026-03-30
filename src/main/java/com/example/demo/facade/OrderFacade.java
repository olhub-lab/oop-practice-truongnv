package com.example.demo.facade;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.order.UpdateOrderRequest;

public interface OrderFacade {
  OrderResponse createOrder(CreateOrderRequest request);

  OrderResponse updateOrder(String orderId, UpdateOrderRequest request);

  OrderResponse getOrder(String orderId);

  void deleteOrder(String orderId);

  PageResponse<OrderResponse> filterOrders(OrderFilterRequest request);
}
