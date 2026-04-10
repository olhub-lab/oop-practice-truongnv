package com.example.demo.payment.provider;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class BankProvider {
  private static final Logger logger = Logger.getLogger(BankProvider.class.getName());

  private static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("10000000");
  private static final String STATUS_SUCCESS = "SUCCESS";
  private static final String STATUS_FAILED = "FAILED";

  public String transfer(String accountNumber, BigDecimal amount, String description) {
    logger.info(() -> "transfer accountNumber=" + accountNumber + ", amount=" + amount);
    String status = amount.compareTo(MAX_TRANSFER_AMOUNT) <= 0 ? STATUS_SUCCESS : STATUS_FAILED;

    logger.info(() -> "transfer result: " + status + " for account " + accountNumber);
    return status;
  }
}
