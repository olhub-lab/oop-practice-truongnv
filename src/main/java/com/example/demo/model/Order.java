package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
  private String orderId;
  private Long customerId;
  private String customerName;
  private BigDecimal amount;
  private PaymentMethod paymentMethod;
  private BigDecimal feeAmount;
  private BigDecimal discountAmount;
  private BigDecimal finalAmount;
  private OrderStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String cancelReason;

  public void calculateFinalAmount() {
    if(this.amount == null || this.paymentMethod == null) return;

    BigDecimal feeRate = this.paymentMethod.getFeeRate();
    BigDecimal discountRate = this.paymentMethod.getDiscountRate();

    this.feeAmount = this.amount.multiply(feeRate);
    this.discountAmount = this.amount.multiply(discountRate);

    this.finalAmount = this.amount.add(this.feeAmount).subtract(this.discountAmount);
  }
}

