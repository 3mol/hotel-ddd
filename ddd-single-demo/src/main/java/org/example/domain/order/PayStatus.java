package org.example.domain.order;

public enum PayStatus {
  PAID, // 已支付
  UNPAID, // 未支付
  REFUNDING, // 退款中
  REFUNDED, // 已退款
  CANCELLED, // 取消，未付款的情况下可以直接转换为取消状态
}
