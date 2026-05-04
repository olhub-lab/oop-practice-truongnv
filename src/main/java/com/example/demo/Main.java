package com.example.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.dto.order.CancelOrderResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.dto.payment.PaymentOrderResponse;
import com.example.demo.facade.OrderFacade;
import com.example.demo.model.enums.PaymentMethod;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
    OrderFacade orderFacade = context.getBean(OrderFacade.class);

    Scanner scanner = new Scanner(System.in);
    boolean running = true;

    while (running) {
      printMenu();
      String choice = scanner.nextLine().trim();

      switch (choice) {
        case "1":
          handleCreateOrder(scanner, orderFacade);
          break;
        case "2":
          handleGetOrder(scanner, orderFacade);
          break;
        case "3":
          handleFilterOrders(scanner, orderFacade);
          break;
        case "4":
          handleCancelOrder(scanner, orderFacade);
          break;
        case "5":
          handleProcessPayment(scanner, orderFacade);
          break;
        case "0":
          running = false;
          System.out.println("Goodbye.");
          break;
        default:
          System.out.println("Invalid choice.");
      }
    }

    scanner.close();
    context.close();
  }

  private static void printMenu() {
    System.out.println("\n ORDER MANAGEMENT");
    System.out.println("1. Create order");
    System.out.println("2. Get order by ID");
    System.out.println("3. List orders");
    System.out.println("4. Cancel order");
    System.out.println("5. Process payment");
    System.out.println("0. Exit");
    System.out.print("Choice: ");
  }

  private static void handleCreateOrder(Scanner scanner, OrderFacade orderFacade) {
    try {
      CreateOrderRequest request = new CreateOrderRequest();

      System.out.print("Customer ID: ");
      request.setCustomerId(Long.parseLong(scanner.nextLine()));

      System.out.print("Customer name: ");
      request.setCustomerName(scanner.nextLine());

      System.out.print("Amount: ");
      request.setAmount(new BigDecimal(scanner.nextLine()));

      request.setPaymentMethod(readPaymentMethod(scanner));

      OrderResponse response = orderFacade.createOrder(request);
      System.out.println("\n✓ Order created:");
      System.out.println("  Order ID    : " + response.getOrderId());
      System.out.println("  Status      : " + response.getStatus());
      System.out.println("  Final amount: " + response.getFinalAmount());
      System.out.println("  Created at  : " + response.getCreatedAt());

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void handleGetOrder(Scanner scanner, OrderFacade orderFacade) {
    try {
      System.out.print("Order ID: ");
      OrderResponse response = orderFacade.getOrder(scanner.nextLine().trim());
      System.out.println("\nOrder details:");
      System.out.println("Order ID: " + response.getOrderId());
      System.out.println("Customer: " + response.getCustomerName());
      System.out.println("Amount: " + response.getAmount());
      System.out.println("Final amount: " + response.getFinalAmount());
      System.out.println("Status: " + response.getStatus());
      System.out.println("Payment method: " + response.getPaymentMethod());
      System.out.println("Created at: " + response.getCreatedAt());

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void handleFilterOrders(Scanner scanner, OrderFacade orderFacade) {
    try {
      List<OrderResponse> orders = orderFacade.filterOrders(new OrderFilterRequest());
      if (orders.isEmpty()) {
        System.out.println("No orders found.");
        return;
      }
      System.out.println("\nOrders (" + orders.size() + "):");
      for (OrderResponse o : orders) {
        System.out.println("  " + o.getOrderId()
            + " | " + o.getStatus()
            + " | " + o.getFinalAmount()
            + " | " + o.getCreatedAt());
      }

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void handleCancelOrder(Scanner scanner, OrderFacade orderFacade) {
    try {
      System.out.print("Order ID: ");
      String orderId = scanner.nextLine().trim();

      System.out.print("Cancel reason: ");
      String reason = scanner.nextLine().trim();

      CancelOrderResponse response = orderFacade.cancelOrder(orderId, reason);
      System.out.println("\nOrder cancelled:");
      System.out.println("Order ID: " + response.getOrderId());
      System.out.println("Status: " + response.getStatus());
      System.out.println("Reason: " + response.getCancelReason());
      System.out.println("Cancelled at: " + response.getCancelTime());

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void handleProcessPayment(Scanner scanner, OrderFacade orderFacade) {
    try {
      System.out.print("Order ID: ");
      PaymentOrderResponse response = orderFacade.processPayment(scanner.nextLine().trim());
      System.out.println("\nPayment processed:");
      System.out.println("Order ID: " + response.getOrderId());
      System.out.println("Status: " + response.getStatus());
      System.out.println("  Processed at: " + response.getProcessedAt());

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static PaymentMethod readPaymentMethod(Scanner scanner) {
    System.out.println("Payment method:\n 1. CREDIT_CARD\n 2. BANK_TRANSFER\n 3. E_WALLET");
    System.out.print("Choice: ");
    switch (scanner.nextLine().trim()) {
      case "1":
        return PaymentMethod.CREDIT_CARD;
      case "2":
        return PaymentMethod.BANK_TRANSFER;
      case "3":
        return PaymentMethod.E_WALLET;
      default:
        throw new IllegalArgumentException("Invalid payment method choice.");
    }
  }
}
