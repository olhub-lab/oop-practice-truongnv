package com.example.demo.payment;

import java.util.Map;
import java.util.logging.Logger;

import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.model.enums.PaymentMethod;

public class PaymentPortFactory implements PaymentPortResolver {
  private static final Logger logger = Logger.getLogger(PaymentPortFactory.class.getName());

  private final Map<PaymentMethod, PaymentPort> portMap;

  public PaymentPortFactory(Map<PaymentMethod, PaymentPort> portMap) {
    this.portMap = portMap;
  }

  @Override
  public PaymentPort getPaymentPort(PaymentMethod paymentMethod) {
    logger.info(() -> "getPaymentPort param: " + paymentMethod);
    PaymentPort port = portMap.get(paymentMethod);
    if (port == null) {
      throw new UnsupportedPaymentMethodException(
          "No payment port for method: " + paymentMethod.name());
    }
    return port;
  }

}
