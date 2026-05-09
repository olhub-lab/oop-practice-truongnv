package com.example.demo.dto.order;

import java.math.BigDecimal;

import com.example.demo.model.enums.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateOrderRequest {

  @NotNull(message = "CustomerId cannot be null")
  @Positive(message = "CustomerId must be > 0")
  private Long customerId;

  @NotBlank(message = "CustomerName cannot be empty")
  @Size(max = 100, message = "CustomerName cannot be longer than 100 characters")
  private String customerName;

  @NotNull(message = "Amount cannot be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
  private BigDecimal amount;

  @NotNull(message = "PaymentMethod cannot be null")
  private PaymentMethod paymentMethod;

  public CreateOrderRequest() {
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}