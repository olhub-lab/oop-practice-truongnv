package com.example.demo.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CancelOrderRequest {
  @NotBlank(message = "Cancel reason cannot be empty")
  @Size(max = 500, message = "Cancel reason cannot exceed 500 characters")
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
