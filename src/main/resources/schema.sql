CREATE TABLE IF NOT EXISTS orders (
  order_id VARCHAR(30) PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  customer_name VARCHAR(100) NOT NULL,
  amount DECIMAL(15, 2) NOT NULL,
  payment_method VARCHAR(20) NOT NULL,
  fee_amount DECIMAL(15, 2),
  discount_amount DECIMAL(15, 2),
  final_amount DECIMAL(15, 2),
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  cancel_reason VARCHAR(500)
);