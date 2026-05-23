package com.example.demo.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

  long nextOrderSequence();

  @Query("""
      SELECT o FROM Order o
      WHERE (:status IS NULL OR o.status = :status)
      AND (:paymentMethod IS NULL OR o.paymentMethod = :paymentMethod)
      AND (:fromDate IS NULL OR o.createdAt >= :fromDate)
      AND (:toDate IS NULL OR o.createdAt <= :toDate)
      ORDER BY o.createdAt DESC
      """)
  List<Order> findAllWithFilter(
      @Param("status") OrderStatus status,
      @Param("paymentMethod") PaymentMethod paymentMethod,
      @Param("fromDate") LocalDateTime fromDate,
      @Param("toDate") LocalDateTime toDate);

}
