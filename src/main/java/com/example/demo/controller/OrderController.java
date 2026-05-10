package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CancelOrderRequest;
import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.exception.InvalidOrderStatusException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.exception.ValidationException;
import com.example.demo.facade.OrderFacade;

import jakarta.servlet.http.HttpServletRequest;

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

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(OrderNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, String>> handleValidation(ValidationException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(InvalidOrderStatusException.class)
  public ResponseEntity<Map<String, String>> handleInvalidStatus(InvalidOrderStatusException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(Map.of("error", e.getMessage()));
  }
}
