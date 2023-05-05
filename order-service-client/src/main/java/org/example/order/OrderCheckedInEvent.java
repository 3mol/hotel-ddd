package org.example.order;

import lombok.Data;
import org.example.room.RoomId;

@Data
public class OrderCheckedInEvent implements OrderDomainEvent {
  private final OrderId orderId;
  private final RoomId roomId;

  public OrderCheckedInEvent(OrderId orderId, RoomId roomId) {
    this.orderId = orderId;
    this.roomId = roomId;
  }
}
