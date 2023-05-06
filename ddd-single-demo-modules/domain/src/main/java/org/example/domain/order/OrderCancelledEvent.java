package org.example.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCancelledEvent implements OrderDomainEvent {
  private OrderId orderId;
  private RoomId roomId;
}
