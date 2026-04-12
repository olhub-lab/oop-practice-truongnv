package com.example.demo.payment.adapter;

import java.util.logging.Logger;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.payment.PaymentPort;
import com.example.demo.payment.provider.MomoProvider;

public class MomoAdapter implements PaymentPort {

  private static final Logger logger = Logger.getLogger(MomoAdapter.class.getName());

  private final MomoProvider momoProvider;

  public MomoAdapter(MomoProvider momoProvider) {
    this.momoProvider = momoProvider;
  }

  @Override
  public OrderStatus process(Order order) {
    logger.info(() -> "process param: orderId=" + order.getOrderId());
    boolean success = momoProvider.requestPayment(
        order.getOrderId(),
        order.getFinalAmount());

    return success ? OrderStatus.SUCCESS : OrderStatus.FAILED;
  }

}
