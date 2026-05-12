package com.example.demo.controller.advice;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class OrderControllerAdvice {

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, String>> handleValidation(ValidationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(UnsupportedPaymentMethodException.class)
  public ResponseEntity<Map<String, String>> handleUnsupportedPayment(UnsupportedPaymentMethodException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
  }

}
