package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CancelOrderRequest;
import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;
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

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
    log.info("Create order request: {}", createOrderRequest);

    OrderResponse orderResponse = orderFacade.createOrder(createOrderRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<CancelOrderResponse> cancelOrder(
      @PathVariable("id") String id,
      @Valid @RequestBody CancelOrderRequest cancelOrderRequest) {
    log.info("cancelOrder request: id = {}", id);

    CancelOrderResponse cancelOrderResponse = orderFacade.cancelOrder(id, cancelOrderRequest.getReason());

    return ResponseEntity.ok(cancelOrderResponse);
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> filterOrders(@ModelAttribute OrderFilterRequest request) {
    log.info("filterOrders request: {}", request);
    return ResponseEntity.ok(orderFacade.filterOrders(request));
  }

  @PostMapping("/{id}/payment")
  public ResponseEntity<PaymentOrderResponse> processPayment(@PathVariable String id) {
    log.info("processPayment param: id = {}", id);

    PaymentOrderResponse paymentOrderResponse = orderFacade.processPayment(id);

    return ResponseEntity.ok(paymentOrderResponse);
  }
}
