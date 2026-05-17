package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.port.PaymentPort;
import com.example.demo.port.PaymentPortResolver;
import com.example.demo.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

    private final OrderRepository orderRepository;
    private final PaymentPortResolver paymentPortResolver;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentPortResolver paymentPortResolver) {
        this.orderRepository = orderRepository;
        this.paymentPortResolver = paymentPortResolver;
    }

    private String generateOrderId(LocalDateTime now) {
        String date = now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
        long sequence = orderRepository.nextOrderSequence();

        return String.format("ORD-%s-%05d", date, sequence);
    }

    @Override
    @Transactional
    public Order create(CreateOrderRequest request) {
        log.info("Creating order with customer name: {}", request.getCustomerName());

        LocalDateTime now = LocalDateTime.now();
        Order order = Order.builder()
                .orderId(generateOrderId(now))
                .customerId(request.getCustomerId())
                .customerName(request.getCustomerName())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();

        orderRepository.save(order);

        return order;
    }

    @Override
    public Order get(String orderId) {
        log.info("Getting order with id: {}", orderId);

        if (!StringUtils.hasText(orderId)) {
            throw new ValidationException("orderId cannot be null or empty.");
        }

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(String orderId, String cancelReason) {
        log.info("cancelOrder param: orderId = {}, reason = {}", orderId, cancelReason);

        if (!StringUtils.hasText(orderId)) {
            throw new ValidationException("orderId must not be empty");
        }

        if (!StringUtils.hasText(cancelReason)) {
            throw new ValidationException("cancelReason must not be empty");
        }

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        order.cancel(cancelReason);
        orderRepository.update(order);

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll(OrderFilterRequest request) {
        OrderFilterRequest filterRequest = request != null ? request : new OrderFilterRequest();
        log.info("findAll request: {}", filterRequest);

        validateFilterRequest(filterRequest);

        return orderRepository.findAll(filterRequest);
    }

    @Override
    @Transactional
    public Order processPayment(String orderId) {
        log.info("processPayment param: orderId = {}", orderId);

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        order.validatePendingStatus();

        PaymentPort port = paymentPortResolver.getPaymentPort(order.getPaymentMethod());
        OrderStatus status = port.process(order);

        order.applyPaymentResult(status);
        orderRepository.update(order);

        return order;
    }

    private void validateFilterRequest(OrderFilterRequest request) {
        if (request.getFromDate() != null
                && request.getToDate() != null
                && request.getFromDate().isAfter(request.getToDate())) {
            throw new ValidationException("fromDate must be before or equal to toDate.");
        }
    }

}
