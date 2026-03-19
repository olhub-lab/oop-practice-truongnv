package com.example.demo;

import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.enums.PaymentMethod;
import com.example.demo.persistence.OrderDAO;
import com.example.demo.persistence.impl.InMemoryOrderDaoImpl;
import com.example.demo.service.OrderService;
import com.example.demo.service.impl.OrderServiceImpl;
import java.math.BigDecimal;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    OrderDAO orderDAO = new InMemoryOrderDaoImpl();
    OrderService orderService = new OrderServiceImpl(orderDAO);
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

      Order order = orderService.createOrder(request);

      System.out.println();
      System.out.println("Order ID: " + order.getOrderId());
      System.out.println("Stauts: " + order.getStatus());
      System.out.println("Final amount: " + order.getFinalAmount());
      System.out.println("Created time: " + order.getCreatedAt());
      System.out.println();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      scanner.close();
    }
  }
}
