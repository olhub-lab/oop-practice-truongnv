package com.example.demo.payment.provider;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class BankProvider {
  private static final Logger logger = LoggerFactory.getLogger(BankProvider.class);

  private static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("10000000");
  private static final String STATUS_SUCCESS = "SUCCESS";
  private static final String STATUS_FAILED = "FAILED";

  public String transfer(String accountNumber, BigDecimal amount, String description) {
    logger.info("transfer accountNumber={}, amount={}", accountNumber, amount);
    String status = amount.compareTo(MAX_TRANSFER_AMOUNT) <= 0 ? STATUS_SUCCESS : STATUS_FAILED;

    logger.info("transfer result: {} for account {}", status, accountNumber);
    return status;
  }
}
