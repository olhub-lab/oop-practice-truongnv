package com.example.demo.payment.provider;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class MomoProvider {
  private static final Logger logger = LoggerFactory.getLogger(MomoProvider.class);
  private static final BigDecimal MAX_PAYMENT_AMOUNT = new BigDecimal("10000000");

  public boolean requestPayment(String walletId, BigDecimal money) {
    logger.info("Requesting payment with walletId: {}, money: {}", walletId, money);
    boolean success = money.compareTo(MAX_PAYMENT_AMOUNT) <= 0;

    logger.info("requestPayment result: {} for wallet {}", success ? "SUCCESS" : "FAILED", walletId);
    return success;
  }
}
