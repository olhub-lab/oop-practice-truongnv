package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.facade.OrderFacade;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> filterOrders(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String paymentMethod,
      @RequestParam(required = false) String fromDate,
      @RequestParam(required = false) String toDate) {
    OrderFilterRequest orderFilterRequest = new OrderFilterRequest();
    if (status != null) {
      orderFilterRequest.setStatus(OrderStatus.valueOf(status));
    }
    if (paymentMethod != null) {
      orderFilterRequest.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
    }
    if (fromDate != null) {
      orderFilterRequest.setFromDate(LocalDateTime.parse(fromDate));
    }
    if (toDate != null) {
      orderFilterRequest.setToDate(LocalDateTime.parse(toDate));
    }
    return ResponseEntity.ok(orderFacade.filterOrders(orderFilterRequest));
  }
}
