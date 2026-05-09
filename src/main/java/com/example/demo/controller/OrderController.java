package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CancelOrderRequest;
import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.facade.OrderFacade;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @PostMapping("/{orderId}/cancel")
  public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable String orderId,
      @RequestBody CancelOrderRequest cancelOrderRequest) {
    CancelOrderResponse cancelOrderResponse = orderFacade.cancelOrder(orderId, cancelOrderRequest.getReason());
    return ResponseEntity.ok(cancelOrderResponse);
  }

}
