package com.example.demo.payment;

import java.util.Map;

import com.example.demo.model.enums.PaymentMethod;

public class PaymentPortFactory {
  private final Map<PaymentMethod, PaymentPort> paymentPortMap;

  public PaymentPortFactory(Map<PaymentMethod, PaymentPort> paymentPortMap) {
    this.paymentPortMap = paymentPortMap;
  }

  public PaymentPort getPaymentPort(PaymentMethod paymentMethod) {
    PaymentPort port = paymentPortMap.get(paymentMethod);
    if (port == null) {
      throw new IllegalArgumentException(
          "No payment provider for method: " + paymentMethod);
    }
    return port;
  }

}
