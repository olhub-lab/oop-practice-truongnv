package com.example.demo.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.adapter.BankPaymentAdapter;
import com.example.demo.adapter.MomoPaymentAdapter;
import com.example.demo.model.enums.PaymentMethod;
import com.example.demo.port.PaymentPort;
import com.example.demo.port.PaymentPortFactory;
import com.example.demo.port.PaymentPortResolver;

@Configuration
public class ApplicationConfig {

  @Bean
  public PaymentPortResolver paymentPortResolver(BankPaymentAdapter bankPaymentAdapter,
      MomoPaymentAdapter momoPaymentAdapter) {
    Map<PaymentMethod, PaymentPort> portMap = new EnumMap<>(PaymentMethod.class);
    portMap.put(PaymentMethod.CREDIT_CARD, bankPaymentAdapter);
    portMap.put(PaymentMethod.BANK_TRANSFER, bankPaymentAdapter);
    portMap.put(PaymentMethod.E_WALLET, momoPaymentAdapter);
    return new PaymentPortFactory(portMap);
  }
}
