package com.example.demo.dto.order;

import java.math.BigDecimal;

import com.example.demo.model.enums.OrderStatus;

public class UpdateOrderRequest {
  private BigDecimal amount;
  private OrderStatus status;
  private String cancelReason;

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }

  public OrderStatus getStatus() { return status; }
  public void setStatus(OrderStatus status) { this.status = status; }

  public String getCancelReason() { return cancelReason; }
  public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
