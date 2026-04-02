package com.example.demo.dto.order;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

public class OrderResponse {
  private String orderId;
  private Long customerId;
  private String customerName;
  private BigDecimal amount;
  private PaymentMethod paymentMethod;
  private BigDecimal feeAmount;
  private BigDecimal discountAmount;
  private BigDecimal finalAmount;
  private OrderStatus status;
  private String createdAt;
  private String updatedAt;
  private String cancelReason;

  
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

  public OrderResponse(Order order){
    this.orderId = order.getOrderId();
    this.customerId = order.getCustomerId();
    this.customerName = order.getCustomerName();
    this.amount = order.getAmount();
    this.paymentMethod = order.getPaymentMethod();
    this.feeAmount = order.getFeeAmount();
    this.discountAmount = order.getDiscountAmount();
    this.finalAmount = order.getFinalAmount();
    this.status = order.getStatus();
    this.createdAt =
        order.getCreatedAt() != null ? order.getCreatedAt().format(DATE_FORMATTER) : null;
    this.updatedAt =
        order.getUpdatedAt() != null ? order.getUpdatedAt().format(DATE_FORMATTER) : null;
    this.cancelReason = order.getCancelReason();
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

  public String getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getCancelReason() {
    return cancelReason;
  }
  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }
}
