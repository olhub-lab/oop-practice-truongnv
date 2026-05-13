package com.example.demo.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

public class PaymentOrderResponse {
  private String orderId;
  private OrderStatus status;
  private PaymentMethod paymentMethod;
  private BigDecimal amount;
  private BigDecimal finalAmount;
  private LocalDateTime processedAt;

  public PaymentOrderResponse(String orderId, OrderStatus status, PaymentMethod paymentMethod, BigDecimal amount,
      BigDecimal finalAmount, LocalDateTime processedAt) {
    this.orderId = orderId;
    this.status = status;
    this.paymentMethod = paymentMethod;
    this.amount = amount;
    this.finalAmount = finalAmount;
    this.processedAt = processedAt;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public void setProcessedAt(LocalDateTime processedAt) {
    this.processedAt = processedAt;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public void setFinalAmount(BigDecimal finalAmount) {
    this.finalAmount = finalAmount;
  }

  public String getProcessedAt() {
    return processedAt.toString();
  }
}
