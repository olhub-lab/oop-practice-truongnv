package com.example.demo.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CancelOrderRequest {
  @NotBlank(message = "cancelReason must not be empty")
  @Size(max = 500, message = "cancelReason must not exceed 500 characters")
  private String reason;

  public CancelOrderRequest() {
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
