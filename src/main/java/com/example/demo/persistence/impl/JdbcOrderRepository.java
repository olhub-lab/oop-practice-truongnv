package com.example.demo.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.order.OrderFilterRequest;
import com.example.demo.model.Order;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.model.enums.PaymentMethod;
import com.example.demo.persistence.OrderRepository;

@Repository
@Primary
public class JdbcOrderRepository implements OrderRepository {

  private static final Logger logger = Logger.getLogger(JdbcOrderRepository.class.getName());

  private static final RowMapper<Order> ORDER_ROW_MAPPER = JdbcOrderRepository::mapRow;

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


  public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  @Override
  public void save(Order order) {
    logger.info(() -> "Saving order: " + order.getOrderId());
    jdbcTemplate.update("""
        INSERT INTO orders (
          order_id, customer_id, customer_name, amount, payment_method,
          fee_amount, discount_amount, final_amount, status,
          created_at, updated_at, cancel_reason
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """,
        order.getOrderId(),
        order.getCustomerId(),
        order.getCustomerName(),
        order.getAmount(),
        order.getPaymentMethod().name(),
        order.getFeeAmount(),
        order.getDiscountAmount(),
        order.getFinalAmount(),
        order.getStatus().name(),
        Timestamp.valueOf(order.getCreatedAt()),
        Timestamp.valueOf(order.getUpdatedAt()),
        order.getCancelReason());
  }

  @Override
  public void update(Order order) {
    logger.info(() -> "Updating order: " + order.getOrderId());
    jdbcTemplate.update("""
        UPDATE orders
        SET customer_id = ?,
            customer_name = ?,
            amount = ?,
            payment_method = ?,
            fee_amount = ?,
            discount_amount = ?,
            final_amount = ?,
            status = ?,
            updated_at = ?,
            cancel_reason = ?
        WHERE order_id = ?
        """,
        order.getCustomerId(),
        order.getCustomerName(),
        order.getAmount(),
        order.getPaymentMethod().name(),
        order.getFeeAmount(),
        order.getDiscountAmount(),
        order.getFinalAmount(),
        order.getStatus().name(),
        Timestamp.valueOf(order.getUpdatedAt()),
        order.getCancelReason(),
        order.getOrderId());
  }

  @Override
  public Order findById(String id) {
    logger.info(() -> "Getting order with id: " + id);
    try {
      return jdbcTemplate.queryForObject(
          "SELECT * FROM orders WHERE order_id = ?",
          ORDER_ROW_MAPPER,
          id);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void delete(String id) {
    logger.info(() -> "Deleting order with id: " + id);
    jdbcTemplate.update("DELETE FROM orders WHERE order_id = ?", id);
  }

  @Override
  public List<Order> findAll(OrderFilterRequest request) {
    logger.info(() -> "Finding all orders with filter: " + request);
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
        sql += " AND DATE(created_at) >= :fromDate";
        params.addValue("fromDate", request.getFromDate());
      }
      if (request.getToDate() != null) {
        sql += " AND DATE(created_at) <= :toDate";
        params.addValue("toDate", request.getToDate());
      }
    }

    sql += " ORDER BY created_at DESC";
    return namedParameterJdbcTemplate.query(sql, params, ORDER_ROW_MAPPER);
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
