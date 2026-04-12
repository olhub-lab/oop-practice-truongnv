package com.example.demo.dto.payment;

import com.example.demo.model.enums.OrderStatus;

public class PaymentOrderResponse {
  private String orderId;
  private OrderStatus status;
  private String processedAt;

  public PaymentOrderResponse(String orderId, OrderStatus status, String processedAt) {
    this.orderId = orderId;
    this.status = status;
    this.processedAt = processedAt;
  }

  public String getOrderId() {
    return orderId;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public String getProcessedAt() {
    return processedAt;
  }
}
