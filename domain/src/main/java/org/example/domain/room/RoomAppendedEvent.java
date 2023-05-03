package org.example.domain.room;

import lombok.Data;
import org.example.domain.order.RoomId;

@Data
public class RoomAppendedEvent implements RoomDomainEvent {
  private final RoomId roomId;

  public RoomAppendedEvent(RoomId roomId) {
    this.roomId = roomId;
  }
}
