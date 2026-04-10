package com.example.demo.exception;

public class UnsupportedPaymentMethodException extends RuntimeException {
  public UnsupportedPaymentMethodException(String message) {
    super(message);
  }
}
