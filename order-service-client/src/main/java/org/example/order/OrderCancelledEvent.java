package org.example.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.room.RoomId;

@Data
@AllArgsConstructor
public class OrderCancelledEvent implements OrderDomainEvent {
  private OrderId orderId;
  private RoomId roomId;
}
