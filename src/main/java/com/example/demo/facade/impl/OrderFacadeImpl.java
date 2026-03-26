package com.example.demo.facade.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.exception.ValidationException;
import com.example.demo.facade.OrderFacade;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.service.OrderService;

public class OrderFacadeImpl implements OrderFacade {

    private static final Logger logger = Logger.getLogger(OrderFacadeImpl.class.getName());
    
    private final OrderService orderService;

    public OrderFacadeImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    private String generateOrderId() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("ORD-%s-%05d", datePart, UUID.randomUUID().toString());
    }

    private void validateCreateRequest(CreateOrderRequest request) {
        if (request == null) {
            throw new ValidationException("Request cannot be null.");
        }
        if (request.getCustomerId() == null || request.getCustomerId() <= 0) {
            throw new ValidationException("CustomerId cannot be null and must be > 0.");
        }
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new ValidationException("CustomerName cannot be empty.");
        }
        if (request.getCustomerName().length() > 100) {
            throw new ValidationException("CustomerName cannot be longer than 100 characters.");
        }
        if (request.getPaymentMethod() == null) {
            throw new ValidationException("PaymentMethod cannot be null.");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than 0.");
        }
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        logger.info(() -> "createOrder param: " + request);

        validateCreateRequest(request);
        LocalDateTime now = LocalDateTime.now();
        Order newOrder = Order.builder()
            .orderId(generateOrderId())
            .customerId(request.getCustomerId())
            .customerName(request.getCustomerName())
            .amount(request.getAmount())
            .paymentMethod(request.getPaymentMethod())
            .status(OrderStatus.PENDING)
            .createdAt(now)
            .updatedAt(now)
            .build(); 
        Order savedOrder = orderService.createOrder(newOrder);

        return new OrderResponse(savedOrder);
    }
}