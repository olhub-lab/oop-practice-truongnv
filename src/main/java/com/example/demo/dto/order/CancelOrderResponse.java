package com.example.demo.dto.order;

import com.example.demo.model.enums.OrderStatus;

public class CancelOrderResponse {
  private String orderId;
  private OrderStatus status;
  private String cancelReason;
  private String cancelTime;

  public CancelOrderResponse(
      String orderId,
      OrderStatus status,
      String cancelReason,
      String cancelTime) {
    this.orderId = orderId;
    this.status = status;
    this.cancelReason = cancelReason;
    this.cancelTime = cancelTime;
  }

  public String getOrderId() {
    return orderId;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public String getCancelTime() {
    return cancelTime;
  }
}