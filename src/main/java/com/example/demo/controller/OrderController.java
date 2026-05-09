package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.payment.PaymentOrderResponse;
import com.example.demo.facade.OrderFacade;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @PostMapping("/{orderId}/payment")
  public ResponseEntity<PaymentOrderResponse> processPayment(@PathVariable String orderId) {
    PaymentOrderResponse paymentOrderResponse = orderFacade.processPayment(orderId);
    return ResponseEntity.ok(paymentOrderResponse);
  }

}
