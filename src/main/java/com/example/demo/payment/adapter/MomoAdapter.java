package com.example.demo.payment.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.payment.PaymentPort;
import com.example.demo.payment.provider.MomoProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MomoAdapter implements PaymentPort {

  private final MomoProvider momoProvider;

  public MomoAdapter(MomoProvider momoProvider) {
    this.momoProvider = momoProvider;
  }

  @Override
  public OrderStatus process(Order order) {
    log.info("process param: orderId={}", order.getOrderId());
    boolean success = momoProvider.requestPayment(
        order.getOrderId(),
        order.getFinalAmount());

    return success ? OrderStatus.SUCCESS : OrderStatus.FAILED;
  }

}
