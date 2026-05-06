package com.example.demo.payment.adapter;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.payment.PaymentPort;
import com.example.demo.payment.provider.BankProvider;

@Component
public class BankAdapter implements PaymentPort {
  private static final Logger logger = Logger.getLogger(BankAdapter.class.getName());
  private static final String SUCCESS_RESPONSE = "SUCCESS";
  private final BankProvider bankProvider;

  public BankAdapter(BankProvider bankProvider) {
    this.bankProvider = bankProvider;
  }

  @Override
  public OrderStatus process(Order order) {
    logger.info(() -> "process param: orderId=" + order.getOrderId());
    String result = bankProvider.transfer(
        order.getOrderId(),
        order.getFinalAmount(),
        "Payment for order " + order.getOrderId());

    return SUCCESS_RESPONSE.equals(result) ? OrderStatus.SUCCESS : OrderStatus.FAILED;
  }
}
