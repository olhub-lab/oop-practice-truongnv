package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.error.ErrorResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.facade.OrderFacade;

import jakarta.servlet.http.HttpServletRequest;

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

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(OrderNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(UnsupportedPaymentMethodException.class)
  public ResponseEntity<Map<String, String>> handleUnsupportedPayment(UnsupportedPaymentMethodException e,
      HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status)
        .body(Map.of("error", e.getMessage()));
  }

}
