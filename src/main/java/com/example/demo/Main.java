package com.example.demo;

import java.math.BigDecimal;
import java.util.Scanner;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.facade.OrderFacade;
import com.example.demo.facade.impl.OrderFacadeImpl;
import com.example.demo.model.enums.PaymentMethod;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.persistence.impl.InMemoryOrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.impl.OrderServiceImpl;

public class Main {

  public static void main(String[] args) {
    OrderRepository orderRepository = new InMemoryOrderRepository();
    OrderService orderService = new OrderServiceImpl(orderRepository);
    OrderFacade orderFacade = new OrderFacadeImpl(orderService);

    Scanner scanner = new Scanner(System.in);

    System.out.println("CREATE ORDER");

    try {
      CreateOrderRequest request = new CreateOrderRequest();

      System.out.print("ID customer: ");
      request.setCustomerId(Long.parseLong(scanner.nextLine()));

      System.out.print("Name customer: ");
      request.setCustomerName(scanner.nextLine());

      System.out.print("Money: ");
      request.setAmount(new BigDecimal(scanner.nextLine()));

      System.out.println("Payment method:");
      System.out.println("  1. CREDIT_CARD");
      System.out.println("  2. BANK_TRANSFER");
      System.out.println("  3. E_WALLET");
      String paymentChoice = scanner.nextLine();

      switch (paymentChoice) {
        case "1":
          request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
          break;
        case "2":
          request.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
          break;
        case "3":
          request.setPaymentMethod(PaymentMethod.E_WALLET);
          break;
        default:
          throw new IllegalArgumentException("Your choice is incorrect.");
      }

      OrderResponse response = orderFacade.createOrder(request);

      System.out.println();
      System.out.println("Order ID: " + response.getOrderId());
      System.out.println("Status: " + response.getStatus());
      System.out.println("Final amount: " + response.getFinalAmount());
      System.out.println("Created time: " + response.getCreatedAt());
      System.out.println();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      scanner.close();
    }
  }
}
