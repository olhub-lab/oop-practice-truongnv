package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CancelOrderRequest;
import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.facade.OrderFacade;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @PostMapping("/{orderId}/cancel")
  public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable String orderId,
      @Valid @RequestBody CancelOrderRequest cancelOrderRequest) {
    String reason = cancelOrderRequest != null ? cancelOrderRequest.getReason() : null;
    log.info("cancelOrder request: orderId={}, hasTextReason={}, lengthReason={}",
        orderId,
        StringUtils.hasText(reason),
        Integer.valueOf(reason != null ? reason.length() : 0));
    CancelOrderResponse cancelOrderResponse = orderFacade.cancelOrder(orderId, reason);
    return ResponseEntity.ok(cancelOrderResponse);
  }
}
