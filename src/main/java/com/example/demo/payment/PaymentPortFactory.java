package com.example.demo.payment;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.model.enums.PaymentMethod;


public class PaymentPortFactory implements PaymentPortResolver {
  private static final Logger logger = LoggerFactory.getLogger(PaymentPortFactory.class);

  private final Map<PaymentMethod, PaymentPort> portMap;

  public PaymentPortFactory(Map<PaymentMethod, PaymentPort> portMap) {
    this.portMap = portMap;
  }

  @Override
  public PaymentPort getPaymentPort(PaymentMethod paymentMethod) {
    logger.info("getPaymentPort param: {}", paymentMethod);
    PaymentPort port = portMap.get(paymentMethod);
    if (port == null) {
      throw new UnsupportedPaymentMethodException(
          "No payment port for method: " + paymentMethod.name());
    }
    return port;
  }

}
