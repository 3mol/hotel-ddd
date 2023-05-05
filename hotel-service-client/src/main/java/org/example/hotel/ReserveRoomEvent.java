package org.example.hotel;

import lombok.Data;

@Data
public class ReserveRoomEvent {
  private String orderNumber;
  private Status status;

  public ReserveRoomEvent(String orderNumber, Status status) {
    this.orderNumber = orderNumber;
    this.status = status;
  }

  public enum Status {
    // 已下单
    // 已付款
    ORDERED,
    PAID
  }
}
