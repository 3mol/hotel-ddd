package org.example.domain.order;

public enum OrderStatus {
  /** 待预定, 未付款 */
  PENDING,
  /** 已预定, 已付订金 */
  RESERVED,
  /** 已入住 */
  CHECKED,
  /** 已退房 */
  CHECKED_OUT,
  /** 已取消 */
  CANCELLED
}
