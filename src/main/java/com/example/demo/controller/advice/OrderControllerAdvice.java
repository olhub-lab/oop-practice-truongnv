package com.example.demo.controller.advice;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.OrderNotFoundException;

@RestControllerAdvice
public class OrderControllerAdvice {

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
  }
}
