package com.example.demo.controller;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.error.ErrorResponse;
import com.example.demo.exception.InvalidOrderStatusException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.UnsupportedPaymentMethodException;
import com.example.demo.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(OrderNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    return ResponseEntity.status(status).body(buildErrorResponse(status, "ORDER_NOT_FOUND", e.getMessage(), request));
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidation(ValidationException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(buildErrorResponse(status, "VALIDATION_ERROR", e.getMessage(), request));
  }

  @ExceptionHandler(InvalidOrderStatusException.class)
  public ResponseEntity<ErrorResponse> handleInvalidOrderStatus(InvalidOrderStatusException e,
      HttpServletRequest request) {
    HttpStatus status = HttpStatus.CONFLICT;
    return ResponseEntity.status(status)
        .body(buildErrorResponse(status, "INVALID_ORDER_STATUS", e.getMessage(), request));
  }

  @ExceptionHandler(UnsupportedPaymentMethodException.class)
  public ResponseEntity<ErrorResponse> handleUnsupportedPayment(UnsupportedPaymentMethodException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status)
        .body(buildErrorResponse(status, "UNSUPPORTED_PAYMENT_METHOD", e.getMessage(), request));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, HttpServletRequest request) {
    logger.error("Unexpected error: [{}] {}", e.getClass().getSimpleName(), e.getMessage(), e);

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(status)
        .body(buildErrorResponse(status, "INTERNAL_SERVER_ERROR", "Something went wrong", request));
  }

  private ErrorResponse buildErrorResponse(HttpStatus status, String code, String message, HttpServletRequest request) {
    return new ErrorResponse(
        LocalDateTime.now(),
        String.valueOf(status.value()),
        status.getReasonPhrase(),
        code,
        message,
        request.getRequestURI());
  }
}
