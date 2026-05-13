package com.example.demo.port;

import java.util.Map;

import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.model.enums.PaymentMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentPortFactory implements PaymentPortResolver {

  private final Map<PaymentMethod, PaymentPort> portMap;

  public PaymentPortFactory(Map<PaymentMethod, PaymentPort> portMap) {
    this.portMap = portMap;
  }

  @Override
  public PaymentPort getPaymentPort(PaymentMethod paymentMethod) {
    log.info("getPaymentPort param: {}", paymentMethod);
    PaymentPort port = portMap.get(paymentMethod);
    if (port == null) {
      throw new UnsupportedPaymentMethodException(
          "No payment port for method: " + paymentMethod.name());
    }
    return port;
  }

}
