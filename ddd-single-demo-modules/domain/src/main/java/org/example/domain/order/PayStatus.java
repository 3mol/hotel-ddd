package org.example.domain.order;

public enum PayStatus {
  PAID, // 已支付
  UNPAID, // 未支付
  REFUNDING, // 退款中
  REFUNDED, // [终态] 已退款
  CANCELLED, // [终态] 取消，未付款的情况下可以直接转换为取消状态
  NO_REFUND, // [终态]不需要退款，取消预约时已经超出可退款期
}
