package com.example.demo.provider;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MomoProvider {
  private static final BigDecimal MAX_PAYMENT_AMOUNT = new BigDecimal("10000000");

  public boolean requestPayment(String walletId, BigDecimal money) {
    log.info("Requesting payment with walletId: {}, money: {}", walletId, money);
    boolean success = money.compareTo(MAX_PAYMENT_AMOUNT) <= 0;

    log.info("requestPayment result: {} for wallet {}", success ? "SUCCESS" : "FAILED", walletId);
    return success;
  }
}
