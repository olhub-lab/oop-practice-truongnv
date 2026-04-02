package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.exception.ValidationException;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

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

  public Order() {
  }

  private Order(OrderBuilder builder) {
    this.orderId = builder.orderId;
    this.customerId = builder.customerId;
    this.customerName = builder.customerName;
    this.amount = builder.amount;
    this.paymentMethod = builder.paymentMethod;
    this.feeAmount = builder.feeAmount;
    this.discountAmount = builder.discountAmount;
    this.finalAmount = builder.finalAmount;
    this.status = builder.status;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
  }

  public void calculateFees() {
    if (this.amount == null || this.paymentMethod == null) {
      return;
    }
    BigDecimal feeRate = this.paymentMethod.getFeeRate();
    BigDecimal discountRate = this.paymentMethod.getDiscountRate();

    this.feeAmount = this.amount.multiply(feeRate);
    this.discountAmount = this.amount.multiply(discountRate);
    this.finalAmount = this.amount.add(this.feeAmount).subtract(this.discountAmount);
  }

  private static final int MAX_CANCEL_REASON_LENGTH = 500;

  public void cancel(String reason) {
    if (this.status != OrderStatus.PENDING) {
      throw new ValidationException(
          String.format("Can not cancel order %s because it in status %s", this.orderId,
              this.status.name()));
    }
    if (reason == null || reason.trim().isEmpty()) {
      throw new ValidationException("Cancel reason cannot be null or empty.");
    }

    if (reason.length() > MAX_CANCEL_REASON_LENGTH) {
      throw new ValidationException("The reason can not be above 500 characters");
    }
    this.status = OrderStatus.CANCELLED;
    this.cancelReason = reason;
    this.updatedAt = LocalDateTime.now();
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
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

  public BigDecimal getFeeAmount() {
    return feeAmount;
  }

  public void setFeeAmount(BigDecimal feeAmount) {
    this.feeAmount = feeAmount;
  }

  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public void setFinalAmount(BigDecimal finalAmount) {
    this.finalAmount = finalAmount;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }

  public static OrderBuilder builder() {
    return new OrderBuilder();
  }

  public static class OrderBuilder {

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

    public OrderBuilder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public OrderBuilder customerId(Long customerId) {
      this.customerId = customerId;
      return this;
    }

    public OrderBuilder customerName(String customerName) {
      this.customerName = customerName;
      return this;
    }

    public OrderBuilder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public OrderBuilder paymentMethod(PaymentMethod paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public OrderBuilder feeAmount(BigDecimal feeAmount) {
      this.feeAmount = feeAmount;
      return this;
    }

    public OrderBuilder discountAmount(BigDecimal discountAmount) {
      this.discountAmount = discountAmount;
      return this;
    }

    public OrderBuilder finalAmount(BigDecimal finalAmount) {
      this.finalAmount = finalAmount;
      return this;
    }

    public OrderBuilder status(OrderStatus status) {
      this.status = status;
      return this;
    }

    public OrderBuilder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public OrderBuilder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Order build() {
      Order order = new Order(this);
      order.calculateFees();
      return order;
    }
  }
}
