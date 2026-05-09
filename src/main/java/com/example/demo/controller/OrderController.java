package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.OrderResponse;
import com.example.demo.facade.OrderFacade;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @DeleteMapping("/{orderId}")
  public ResponseEntity<OrderResponse> deleteOrder(@PathVariable String orderId) {
    orderFacade.deleteOrder(orderId);
    return ResponseEntity.noContent().build();
  }

}
