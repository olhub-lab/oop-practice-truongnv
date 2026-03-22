package com.example.demo.dto.response;

public class CancelOrderResponse {

  private String orderId;
  private String oldStatus;
  private String newStatus;
  private String cancelReason;
  private String canceledAt;
  private String message;

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getOldStatus() {
    return oldStatus;
  }

  public void setOldStatus(String oldStatus) {
    this.oldStatus = oldStatus;
  }

  public String getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(String newStatus) {
    this.newStatus = newStatus;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }

  public String getCanceledAt() {
    return canceledAt;
  }

  public void setCanceledAt(String canceledAt) {
    this.canceledAt = canceledAt;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
