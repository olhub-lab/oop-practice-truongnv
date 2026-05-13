package com.example.demo.port;

import com.example.demo.model.enums.PaymentMethod;

public interface PaymentPortResolver {
  PaymentPort getPaymentPort(PaymentMethod paymentMethod);
}
