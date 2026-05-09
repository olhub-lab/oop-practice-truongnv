package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.order.UpdateOrderRequest;
import com.example.demo.facade.OrderFacade;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<OrderResponse> updateOrder(@PathVariable String orderId,
      @RequestBody UpdateOrderRequest updateOrderRequest) {
    OrderResponse orderResponse = orderFacade.updateOrder(orderId, updateOrderRequest);
    return ResponseEntity.ok(orderResponse);
  }

}
