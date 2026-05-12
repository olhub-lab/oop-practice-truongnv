package com.example.demo.persistence.impl;

import static com.example.demo.persistence.sql.OrderSql.DELETE_ORDER_BY_ID_SQL;
import static com.example.demo.persistence.sql.OrderSql.FILTER_BY_FROM_DATE_SQL;
import static com.example.demo.persistence.sql.OrderSql.FILTER_BY_PAYMENT_METHOD_SQL;
import static com.example.demo.persistence.sql.OrderSql.FILTER_BY_STATUS_SQL;
import static com.example.demo.persistence.sql.OrderSql.FILTER_BY_TO_DATE_SQL;
import static com.example.demo.persistence.sql.OrderSql.FIND_ALL_ORDERS_BASE_SQL;
import static com.example.demo.persistence.sql.OrderSql.FIND_LAST_ORDER_SEQUENCE_SQL;
import static com.example.demo.persistence.sql.OrderSql.FIND_ORDER_BY_ID_SQL;
import static com.example.demo.persistence.sql.OrderSql.INSERT_ORDER_SQL;
import static com.example.demo.persistence.sql.OrderSql.ORDER_BY_CREATED_AT_DESC_SQL;
import static com.example.demo.persistence.sql.OrderSql.UPDATE_ORDER_SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JdbcOrderRepository implements OrderRepository {

  @NonNull
  private static final RowMapper<Order> ORDER_ROW_MAPPER = JdbcOrderRepository::mapRow;

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public JdbcOrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public long nextOrderSequence() {
    log.info("get next order sequence");

    Long lastSequence = namedParameterJdbcTemplate.queryForObject(
        FIND_LAST_ORDER_SEQUENCE_SQL,
        new MapSqlParameterSource(),
        Long.class);

    return (lastSequence != null ? lastSequence : 0L) + 1;
  }

  @Override
  public void save(Order order) {
    log.info("Saving order: {}", order.getOrderId());

    namedParameterJdbcTemplate.update(INSERT_ORDER_SQL, buildParams(order));
  }

  @Override
  public void update(Order order) {
    log.info("Updating order: {}", order.getOrderId());

    int row = namedParameterJdbcTemplate.update(UPDATE_ORDER_SQL, buildParams(order));

    if (row == 0) {
      throw new OrderNotFoundException(order.getOrderId());
    }
  }

  @Override
  public Order findById(String id) {
    log.info("Getting order with id: {}", id);

    try {
      return namedParameterJdbcTemplate.queryForObject(
          FIND_ORDER_BY_ID_SQL,
          new MapSqlParameterSource().addValue("orderId", id),
          ORDER_ROW_MAPPER);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void delete(String id) {
    log.info("Deleting order with id: {}", id);

    namedParameterJdbcTemplate.update(DELETE_ORDER_BY_ID_SQL, new MapSqlParameterSource().addValue("id", id));
  }

  @Override
  public List<Order> findAll(OrderFilterRequest request) {
    log.info("Finding all orders with filter: {}", request);

    String sql = FIND_ALL_ORDERS_BASE_SQL;
    MapSqlParameterSource params = new MapSqlParameterSource();

    if (request != null) {
      if (request.getStatus() != null) {
        sql += FILTER_BY_STATUS_SQL;
        params.addValue("status", request.getStatus().name());
      }
      if (request.getPaymentMethod() != null) {
        sql += FILTER_BY_PAYMENT_METHOD_SQL;
        params.addValue("paymentMethod", request.getPaymentMethod().name());
      }
      if (request.getFromDate() != null) {
        sql += FILTER_BY_FROM_DATE_SQL;
        params.addValue("fromDate", Timestamp.valueOf(request.getFromDate()));
      }
      if (request.getToDate() != null) {
        sql += FILTER_BY_TO_DATE_SQL;
        params.addValue("toDate", Timestamp.valueOf(request.getToDate()));
      }
    }

    sql += ORDER_BY_CREATED_AT_DESC_SQL;
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
