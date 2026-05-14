package com.example.demo.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.port.PaymentPort;
import com.example.demo.provider.BankProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BankPaymentAdapter implements PaymentPort {
  private static final String SUCCESS_RESPONSE = "SUCCESS";
  private final BankProvider bankProvider;

  public BankPaymentAdapter(BankProvider bankProvider) {
    this.bankProvider = bankProvider;
  }

  @Override
  public OrderStatus process(Order order) {
    log.info("process param: orderId= {}", order.getOrderId());
    String result = bankProvider.transfer(
        order.getOrderId(),
        order.getFinalAmount(),
        "Payment for order " + order.getOrderId());

    return SUCCESS_RESPONSE.equals(result) ? OrderStatus.SUCCESS : OrderStatus.FAILED;
  }
}
