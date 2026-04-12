package com.example.demo.payment;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;

public interface PaymentPort {
  OrderStatus process(Order order);
}
