package com.example.demo.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Order;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;

public class OrderServiceImpl implements OrderService {

  private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Order create(Order order) {
    logger.info(() -> "Creating order with id " + order.getCustomerId());

    orderRepository.save(order);

    return order;
  }

  @Override
  public Order update(Order order) {
    logger.info(() -> "Updating order with id " + order.getOrderId());

    orderRepository.update(order);

    return order;
  }

  @Override
  public Order get(String orderId) {
    logger.info(() -> "service get order with id " + orderId);
    Order order = orderRepository.get(orderId);

    if (order == null) {
      throw new OrderNotFoundException("Order not found with id " + orderId);
    }
    return order;
  }

  @Override
  public void delete(String orderId) {

  }

  @Override
  public PageResponse<Order> findAll(OrderFilterRequest request) {
    logger.info(() -> "listOrders param: page=" + request.getPage()
        + ", size=" + request.getSize() + ", status=" + request.getStatus());

    if (request.getFromDate() != null && request.getToDate() != null
        && request.getFromDate().isAfter(request.getToDate())) {
      throw new ValidationException("fromDate must be before or equal to toDate");
    }

    List<Order> allOrders = orderRepository.findAll();
    Stream<Order> stream = allOrders.stream();

    if (request.getCustomerId() != null) {
      stream = stream.filter(o -> o.getCustomerId().equals(request.getCustomerId()));
    }
    if (request.getStatus() != null) {
      stream = stream.filter(o -> o.getStatus() == request.getStatus());
    }
    if (request.getPaymentMethod() != null) {
      stream = stream.filter(o -> o.getPaymentMethod() == request.getPaymentMethod());
    }
    if (request.getFromDate() != null) {
      stream = stream.filter(o -> !o.getCreatedAt().toLocalDate().isBefore(request.getFromDate()));
    }
    if (request.getToDate() != null) {
      stream = stream.filter(o -> !o.getCreatedAt().toLocalDate().isAfter(request.getToDate()));
    }

    Comparator<Order> comparator;
    switch (request.getSortBy().toUpperCase()) {
      case "AMOUNT":
        comparator = Comparator.comparing(Order::getFinalAmount);
        break;
      case "CUSTOMER_NAME":
        comparator = Comparator.comparing(Order::getCustomerName);
        break;
      case "CREATED_AT":
      default:
        comparator = Comparator.comparing(Order::getCreatedAt);
        break;
    }
    if ("DESC".equalsIgnoreCase(request.getSortDirection())) {
      comparator = comparator.reversed();
    }

    List<Order> filteredAndSorted = stream.sorted(comparator).collect(Collectors.toList());

    int totalElements = filteredAndSorted.size();
    int totalPages = (int) Math.ceil((double) totalElements / request.getSize());
    int skip = request.getPage() * request.getSize();

    List<Order> content = filteredAndSorted.stream()
        .skip(skip)
        .limit(request.getSize())
        .collect(Collectors.toList());

    boolean hasNext = (request.getPage() + 1) < totalPages;
    boolean hasPrevious = request.getPage() > 0;

    return new PageResponse<>(content, totalElements, totalPages, hasNext, hasPrevious);
  }
}