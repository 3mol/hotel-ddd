package org.example.domain.order;

import lombok.Data;

@Data
public class OrderCheckedInEvent implements OrderDomainEvent {
  private final OrderId orderId;
  private final RoomId roomId;

  public OrderCheckedInEvent(OrderId orderId, RoomId roomId) {
    this.orderId = orderId;
    this.roomId = roomId;
  }
}
