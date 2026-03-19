package com.example.demo.dto.request;

import com.example.demo.model.enums.PaymentMethod;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateOrderRequest {
  private Long customerId;
  private String customerName;
  private BigDecimal amount;
  private PaymentMethod paymentMethod;
}
