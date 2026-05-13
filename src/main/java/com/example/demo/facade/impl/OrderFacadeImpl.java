package com.example.demo.facade.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;
import com.example.demo.facade.OrderFacade;
import com.example.demo.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderFacadeImpl implements OrderFacade {

  private final OrderService orderService;

  public OrderFacadeImpl(OrderService orderService) {
    this.orderService = orderService;
  }

  @Override
  public OrderResponse createOrder(CreateOrderRequest request) {
    log.info("Creating order for customer: {}", request.getCustomerName());

    return orderService.createOrder(request);
  }

  @Override
  public OrderResponse getOrder(String orderId) {
    log.info("Getting order with id {}", orderId);

    return orderService.getOrder(orderId);
  }

  @Override
  public List<OrderResponse> filterOrders(OrderFilterRequest request) {
    log.info("Filtering orders param: {}", request);
    
    return orderService.findAll(request);
  }

  @Override
  public CancelOrderResponse cancelOrder(String orderId, String cancelReason) {
    log.info("cancelOrder param: orderId = {}", orderId);

    return orderService.cancelOrder(orderId, cancelReason);
  }

  @Override
  public PaymentOrderResponse processPayment(String orderId) {
    log.info("processPayment param: orderId = {}", orderId);

    return orderService.processPayment(orderId);
  }
}
