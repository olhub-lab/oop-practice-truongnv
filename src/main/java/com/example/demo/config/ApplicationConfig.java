package com.example.demo.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.facade.OrderFacade;
import com.example.demo.facade.impl.OrderFacadeImpl;
import com.example.demo.model.enums.PaymentMethod;
import com.example.demo.payment.PaymentPort;
import com.example.demo.payment.PaymentPortFactory;
import com.example.demo.payment.PaymentPortResolver;
import com.example.demo.payment.adapter.BankAdapter;
import com.example.demo.payment.adapter.MomoAdapter;
import com.example.demo.persistence.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.impl.OrderServiceImpl;

@Configuration
public class ApplicationConfig {

  public OrderFacade orderFacade(OrderService orderService) {
    return new OrderFacadeImpl(orderService);
  }

  public OrderService orderService(OrderRepository orderRepository, PaymentPortResolver paymentPortResolver) {
    return new OrderServiceImpl(orderRepository, paymentPortResolver);
  }

  @Bean
  public PaymentPortResolver paymentPortResolver(BankAdapter bankAdapter, MomoAdapter momoAdapter) {
    Map<PaymentMethod, PaymentPort> portMap = new EnumMap<>(PaymentMethod.class);
    portMap.put(PaymentMethod.CREDIT_CARD, bankAdapter);
    portMap.put(PaymentMethod.BANK_TRANSFER, bankAdapter);
    portMap.put(PaymentMethod.E_WALLET, momoAdapter);
    return new PaymentPortFactory(portMap);
  }
}
