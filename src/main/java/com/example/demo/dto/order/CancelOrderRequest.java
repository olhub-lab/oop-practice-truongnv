package com.example.demo.dto.order;

public class CancelOrderRequest {
  private String reason;
  public CancelOrderRequest() {}
  public String getReason() {
    return reason;
  }
  public void setReason(String reason) {
    this.reason = reason;
  }
}
