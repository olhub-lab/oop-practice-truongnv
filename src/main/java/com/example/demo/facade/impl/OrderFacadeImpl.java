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
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.port.PaymentPort;
import com.example.demo.port.PaymentPortResolver;
import com.example.demo.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderFacadeImpl implements OrderFacade {

  private final OrderService orderService;
  private final PaymentPortResolver paymentPortResolver;

  public OrderFacadeImpl(OrderService orderService, PaymentPortResolver paymentPortResolver) {
    this.orderService = orderService;
    this.paymentPortResolver = paymentPortResolver;
  }

  @Override
  public OrderResponse createOrder(CreateOrderRequest request) {
    log.info("Creating order for customer: {}", request.getCustomerName());

    Order order = orderService.create(request);

    return new OrderResponse(order);
  }

  @Override
  public OrderResponse getOrder(String orderId) {
    log.info("Getting order with id {}", orderId);

    Order order = orderService.get(orderId);

    return new OrderResponse(order);
  }

  @Override
  public List<OrderResponse> filterOrders(OrderFilterRequest request) {
    log.info("Filtering orders with request: {}", request);

    return orderService.findAll(request).stream()
        .map(OrderResponse::new)
        .collect(Collectors.toList());
  }

  @Override
  public CancelOrderResponse cancelOrder(String orderId, String cancelReason) {
    log.info("cancelOrder param: orderId = {}", orderId);

    Order order = orderService.cancelOrder(orderId, cancelReason);

    return new CancelOrderResponse(order);
  }

  @Override
  public PaymentOrderResponse processPayment(String orderId) {
    log.info("processPayment param: orderId = {}", orderId);

    Order order = orderService.get(orderId);
    order.validatePendingStatus();

    PaymentPort paymentPort = paymentPortResolver.getPaymentPort(order.getPaymentMethod());

    OrderStatus status = paymentPort.process(order);

    orderService.applyPaymentResult(orderId, status);

    Order updatedOrder = orderService.get(orderId);

    return new PaymentOrderResponse(updatedOrder);
  }
}
