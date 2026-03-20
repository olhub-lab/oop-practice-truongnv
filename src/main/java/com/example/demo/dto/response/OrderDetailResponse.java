package com.example.demo.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailResponse {
  private String orderId;
  private Long customerId;
  private String customerName;
  private BigDecimal amount;
  private String paymentMethod;
  private BigDecimal feeAmount;
  private BigDecimal discountAmount;
  private BigDecimal finalAmount;
  private String status;

  private String createdAt;
  private String updatedAt;

}
