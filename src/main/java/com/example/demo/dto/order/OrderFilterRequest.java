package com.example.demo.dto.order;

import java.math.BigDecimal;

import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

public class OrderFilterRequest {
  private String customerName;
  private OrderStatus status;
  private PaymentMethod paymentMethod;
  private BigDecimal minAmount;
  private BigDecimal maxAmount;

  public String getCustomerName() { return customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }

  public OrderStatus getStatus() { return status; }
  public void setStatus(OrderStatus status) { this.status = status; }

  public PaymentMethod getPaymentMethod() { return paymentMethod; }
  public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

  public BigDecimal getMinAmount() { return minAmount; }
  public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }

  public BigDecimal getMaxAmount() { return maxAmount; }
  public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
}
