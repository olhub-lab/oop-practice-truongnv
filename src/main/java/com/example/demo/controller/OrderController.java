package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.OrderResponse;
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

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
    log.info("Received get order request: id={}", id);

    OrderResponse orderResponse = orderFacade.getOrder(id);
    return ResponseEntity.ok(orderResponse);
  }
}
