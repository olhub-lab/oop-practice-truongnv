package com.example.demo.dto.order;

import com.example.demo.model.enums.OrderStatus;

public class CancelOrderResponse {
    private String orderId;
    private OrderStatus previousStatus;
    private OrderStatus currentStatus;
    private String cancelTime;
    private String message;

    public CancelOrderResponse(String orderId, OrderStatus previousStatus, OrderStatus currentStatus, String cancelTime,
            String message) {
        this.orderId = orderId;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
        this.cancelTime = cancelTime;
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getPreviousStatus() {
        return previousStatus;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public String getMessage() {
        return message;
    }
}