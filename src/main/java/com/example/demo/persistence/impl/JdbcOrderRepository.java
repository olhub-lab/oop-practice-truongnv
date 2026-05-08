package com.example.demo.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;
import com.example.demo.persistence.OrderRepository;

import io.micrometer.common.lang.NonNull;

@Repository
public class JdbcOrderRepository implements OrderRepository {

  private static final Logger logger = LoggerFactory.getLogger(JdbcOrderRepository.class);

  @NonNull
  private static final RowMapper<Order> ORDER_ROW_MAPPER = JdbcOrderRepository::mapRow;

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public JdbcOrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public void save(Order order) {
    logger.info("Saving order: {}", order.getOrderId());
    namedParameterJdbcTemplate.update("""
        INSERT INTO orders (
          order_id, customer_id, customer_name, amount, payment_method,
          fee_amount, discount_amount, final_amount, status,
          created_at, updated_at, cancel_reason
        ) VALUES (:orderId, :customerId, :customerName, :amount, :paymentMethod,
          :feeAmount, :discountAmount, :finalAmount, :status, :createdAt, :updatedAt, :cancelReason)
        """,
        buildParams(order));
  }

  @Override
  public void update(Order order) {
    logger.info("Updating order: {}", order.getOrderId());
    int row = namedParameterJdbcTemplate.update("""
        UPDATE orders
        SET customer_id = :customerId,
            customer_name = :customerName,
            amount = :amount,
            payment_method = :paymentMethod,
            fee_amount = :feeAmount,
            discount_amount = :discountAmount,
            final_amount = :finalAmount,
            status = :status,
            updated_at = :updatedAt,
            cancel_reason = :cancelReason
        WHERE order_id = :orderId
        """,
        buildParams(order));

    if (row == 0)
      throw new OrderNotFoundException(order.getOrderId());
  }

  @Override
  public Order findById(String id) {
    logger.info("Getting order with id: {}", id);
    try {
      return namedParameterJdbcTemplate.queryForObject(
          "SELECT * FROM orders WHERE order_id = :orderId",
          new MapSqlParameterSource().addValue("orderId", id),
          ORDER_ROW_MAPPER);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void delete(String id) {
    logger.info("Deleting order with id: {}", id);
    namedParameterJdbcTemplate.update("DELETE FROM orders WHERE order_id = :id", new MapSqlParameterSource().addValue("id", id));
  }

  @Override
  public List<Order> findAll(OrderFilterRequest request) {
    logger.info("Finding all orders with filter: {}", request);
    String sql = "SELECT * FROM orders WHERE 1=1";
    MapSqlParameterSource params = new MapSqlParameterSource();

    if (request != null) {
      if (request.getStatus() != null) {
        sql += " AND status = :status";
        params.addValue("status", request.getStatus().name());
      }
      if (request.getPaymentMethod() != null) {
        sql += " AND payment_method = :paymentMethod";
        params.addValue("paymentMethod", request.getPaymentMethod().name());
      }
      if (request.getFromDate() != null) {
        sql += " AND created_at >= :fromDate";
        params.addValue("fromDate", Timestamp.valueOf(request.getFromDate()));
      }
      if (request.getToDate() != null) {
        sql += " AND created_at <= :toDate";
        params.addValue("toDate", Timestamp.valueOf(request.getToDate()));
      }
    }

    sql += " ORDER BY created_at DESC";
    return namedParameterJdbcTemplate.query(sql, params, ORDER_ROW_MAPPER);
  }

  @NonNull
  private MapSqlParameterSource buildParams(Order order) {
    return new MapSqlParameterSource()
        .addValue("orderId", order.getOrderId())
        .addValue("customerId", order.getCustomerId())
        .addValue("customerName", order.getCustomerName())
        .addValue("amount", order.getAmount())
        .addValue("paymentMethod", order.getPaymentMethod().name())
        .addValue("feeAmount", order.getFeeAmount())
        .addValue("discountAmount", order.getDiscountAmount())
        .addValue("finalAmount", order.getFinalAmount())
        .addValue("status", order.getStatus().name())
        .addValue("updatedAt", Timestamp.valueOf(order.getUpdatedAt()))
        .addValue("createdAt", order.getCreatedAt() != null
            ? Timestamp.valueOf(order.getCreatedAt())
            : null)
        .addValue("cancelReason", order.getCancelReason());
  }

  private static Order mapRow(ResultSet rs, int rowNum) throws SQLException {
    Order order = new Order();
    order.setOrderId(rs.getString("order_id"));
    order.setCustomerId(rs.getLong("customer_id"));
    order.setCustomerName(rs.getString("customer_name"));
    order.setAmount(rs.getBigDecimal("amount"));
    order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
    order.setFeeAmount(rs.getBigDecimal("fee_amount"));
    order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
    order.setFinalAmount(rs.getBigDecimal("final_amount"));
    order.setStatus(OrderStatus.valueOf(rs.getString("status")));
    Timestamp createdAt = rs.getTimestamp("created_at");
    order.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

    Timestamp updatedAt = rs.getTimestamp("updated_at");
    order.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

    order.setCancelReason(rs.getString("cancel_reason"));
    return order;
  }
}
