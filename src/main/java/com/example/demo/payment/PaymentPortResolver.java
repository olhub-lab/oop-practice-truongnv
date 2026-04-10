package com.example.demo.payment;

import com.example.demo.model.enums.PaymentMethod;

public interface PaymentPortResolver {
  PaymentPort getPaymentPort(PaymentMethod paymentMethod);
}
