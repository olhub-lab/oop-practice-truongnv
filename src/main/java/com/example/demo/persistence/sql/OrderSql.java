package com.example.demo.persistence.sql;

public final class OrderSql {

  public static final String INSERT_ORDER_SQL = """
      INSERT INTO orders (
        order_id, customer_id, customer_name, amount, payment_method,
        fee_amount, discount_amount, final_amount, status,
        created_at, updated_at, cancel_reason
      ) VALUES (:orderId, :customerId, :customerName, :amount, :paymentMethod,
        :feeAmount, :discountAmount, :finalAmount, :status, :createdAt, :updatedAt, :cancelReason)
      """;

  public static final String UPDATE_ORDER_SQL = """
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
      """;

  public static final String FIND_ORDER_BY_ID_SQL = "SELECT * FROM orders WHERE order_id = :orderId";

  public static final String DELETE_ORDER_BY_ID_SQL = "DELETE FROM orders WHERE order_id = :id";

  public static final String FIND_LAST_ORDER_SEQUENCE_SQL = """
      SELECT COALESCE(MAX(CAST(SUBSTRING(order_id, LENGTH(order_id) - 4, 5) AS INTEGER)), 0)
      FROM orders
      WHERE order_id LIKE 'ORD-%'
      """;

  public static final String FIND_ALL_ORDERS_BASE_SQL = "SELECT * FROM orders WHERE 1=1";

  public static final String FILTER_BY_STATUS_SQL = " AND status = :status";

  public static final String FILTER_BY_PAYMENT_METHOD_SQL = " AND payment_method = :paymentMethod";

  public static final String FILTER_BY_FROM_DATE_SQL = " AND created_at >= :fromDate";

  public static final String FILTER_BY_TO_DATE_SQL = " AND created_at <= :toDate";

  public static final String ORDER_BY_CREATED_AT_DESC_SQL = " ORDER BY created_at DESC";

  private OrderSql() {
  }
}
