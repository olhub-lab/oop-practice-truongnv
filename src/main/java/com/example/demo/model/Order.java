package com.example.demo.model;

import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
}
