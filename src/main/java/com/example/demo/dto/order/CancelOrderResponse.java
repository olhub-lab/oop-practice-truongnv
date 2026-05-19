package com.example.demo.dto.order;

import java.time.LocalDateTime;

import com.example.demo.model.enums.OrderStatus;

public class CancelOrderResponse {
  private String orderId;
  private OrderStatus status;
  private String cancelReason;
  private LocalDateTime cancelTime;

  public CancelOrderResponse(
      String orderId,
      OrderStatus status,
      String cancelReason,
      LocalDateTime cancelTime) {
    this.orderId = orderId;
    this.status = status;
    this.cancelReason = cancelReason;
    this.cancelTime = cancelTime;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }

  public void setCancelTime(LocalDateTime cancelTime) {
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
    return cancelTime.toString();
  }
}