package org.example.domain.order;

import lombok.Data;

@Data
public class OrderCheckedOutEvent implements OrderDomainEvent {
  private final OrderId orderId;
  private final RoomId roomId;

  public OrderCheckedOutEvent(OrderId orderId, RoomId roomId) {
    this.orderId = orderId;
    this.roomId = roomId;
  }
}
