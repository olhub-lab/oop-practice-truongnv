package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.payment.PaymentOrderResponse;
import com.example.demo.facade.OrderFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @PostMapping("/{orderId}/payment")
  public ResponseEntity<PaymentOrderResponse> processPayment(@PathVariable String orderId) {
    log.info("processPayment param: orderId = {}", orderId);

    PaymentOrderResponse paymentOrderResponse = orderFacade.processPayment(orderId);
    return ResponseEntity.ok(paymentOrderResponse);
  }

}
