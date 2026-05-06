package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.order.UpdateOrderRequest;
import com.example.demo.facade.OrderFacade;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  @PostMapping
  public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
    OrderResponse orderResponse = orderFacade.createOrder(createOrderRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse.getOrderId());
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
    OrderResponse orderResponse = orderFacade.getOrder(orderId);
    return ResponseEntity.ok(orderResponse);
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> filterOrders(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String paymentMethod,
      @RequestParam(required = false) String fromDate,
      @RequestParam(required = false) String toDate) {
    OrderFilterRequest orderFilterRequest = new OrderFilterRequest();
    if (status != null) {
      orderFilterRequest.setStatus(OrderStatus.valueOf(status));
    }
    if (paymentMethod != null) {
      orderFilterRequest.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
    }
    if (fromDate != null) {
      orderFilterRequest.setFromDate(LocalDate.parse(fromDate));
    }
    if (toDate != null) {
      orderFilterRequest.setToDate(LocalDate.parse(toDate));
    }
    return ResponseEntity.ok(orderFacade.filterOrders(orderFilterRequest));
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<OrderResponse> updateOrder(@PathVariable String orderId,
      @RequestBody UpdateOrderRequest updateOrderRequest) {
    OrderResponse orderResponse = orderFacade.updateOrder(orderId, updateOrderRequest);
    return ResponseEntity.ok(orderResponse);
  }

  @DeleteMapping("/{orderId}")
  public ResponseEntity<OrderResponse> deleteOrder(@PathVariable String orderId) {
    orderFacade.deleteOrder(orderId);
    return ResponseEntity.noContent().build();
  }

}
