package com.example.demo.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.port.PaymentPort;
import com.example.demo.provider.MomoProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MomoPaymentAdapter implements PaymentPort {

  private final MomoProvider momoProvider;

  public MomoPaymentAdapter(MomoProvider momoProvider) {
    this.momoProvider = momoProvider;
  }

  @Override
  public OrderStatus process(Order order) {
    log.info("process param: orderId = {}", order.getOrderId());
    boolean success = momoProvider.requestPayment(
        order.getOrderId(),
        order.getFinalAmount());

    return success ? OrderStatus.SUCCESS : OrderStatus.FAILED;
  }

}
