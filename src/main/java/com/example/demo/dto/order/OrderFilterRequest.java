package com.example.demo.dto.order;

import java.time.LocalDate;

import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

public class OrderFilterRequest {
  
  private OrderStatus status;
  private PaymentMethod paymentMethod;
  private LocalDate fromDate;
  private LocalDate toDate;

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

}
