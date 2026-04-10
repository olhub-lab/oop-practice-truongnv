package com.example.demo.payment.provider;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class MomoProvider {
  private static final Logger logger = Logger.getLogger(MomoProvider.class.getName());
  private static final BigDecimal MAX_PAYMENT_AMOUNT = new BigDecimal("10000000");

  public boolean requestPayment(String walletId, BigDecimal money) {
    logger.info(() -> "Requesting payment with walletId: " + walletId + ", money: " + money);
    boolean success = money.compareTo(MAX_PAYMENT_AMOUNT) <= 0;

    logger.info(() -> "requestPayment result: " + (success ? "SUCCESS" : "FAILED")
        + " for wallet " + walletId);
    return success;
  }
}
