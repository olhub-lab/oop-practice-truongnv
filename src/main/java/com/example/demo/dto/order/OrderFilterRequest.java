package com.example.demo.dto.order;

import java.time.LocalDate;

import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

public class OrderFilterRequest {
  private Long customerId;
  private OrderStatus status;
  private PaymentMethod paymentMethod;
  private LocalDate fromDate;
  private LocalDate toDate;

  private int page = 0;
  private int size = 10;

  private String sortBy = "CREATED_AT";
  private String sortDirection = "DESC";

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

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

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = (size > 100) ? 100 : Math.max(1, size);
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public String getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(String sortDirection) {
    this.sortDirection = sortDirection;
  }
}
