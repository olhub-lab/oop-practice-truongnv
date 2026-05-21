package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

  private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
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

    return orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));
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

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));

    order.cancel(cancelReason);
    orderRepository.save(order);

    return order;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Order> findAll(OrderFilterRequest request) {
    OrderFilterRequest filterRequest = request != null ? request : new OrderFilterRequest();
    log.info("findAll request: {}", filterRequest);

    validateFilterRequest(filterRequest);

    Specification<Order> spec = buildFilterSpecification(filterRequest);

    return orderRepository.findAll(spec);
  }

  @Override
  @Transactional
  public void applyPaymentResult(String orderId, OrderStatus status) {
    log.info("applyPaymentResult param: orderId = {}, status = {}", orderId, status);

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));

    order.applyPaymentResult(status);

    orderRepository.save(order);
  }

  private Specification<Order> buildFilterSpecification(OrderFilterRequest filterRequest) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filterRequest.getStatus() != null) {
        predicates.add(cb.equal(root.get("status"), filterRequest.getStatus()));
      }
      if (filterRequest.getPaymentMethod() != null) {
        predicates.add(cb.equal(root.get("paymentMethod"), filterRequest.getPaymentMethod()));
      }
      if (filterRequest.getFromDate() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filterRequest.getFromDate()));
      }
      if (filterRequest.getToDate() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filterRequest.getToDate()));
      }

      query.orderBy(cb.desc(root.get("createdAt")));

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private void validateFilterRequest(OrderFilterRequest request) {
    if (request.getFromDate() != null
        && request.getToDate() != null
        && request.getFromDate().isAfter(request.getToDate())) {
      throw new ValidationException("fromDate must be before or equal to toDate.");
    }
  }

}
