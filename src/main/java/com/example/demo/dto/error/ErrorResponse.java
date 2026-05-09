package com.example.demo.dto.error;

import java.time.LocalDateTime;

public class ErrorResponse {
  private LocalDateTime timestamp;
  private String status;
  private String error;
  private String code;
  private String message;
  private String path;

  public ErrorResponse(LocalDateTime timestamp, String status, String error, String code, String message, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.code = code;
    this.message = message;
    this.path = path;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
