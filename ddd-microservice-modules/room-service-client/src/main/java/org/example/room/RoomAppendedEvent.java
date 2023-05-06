package org.example.room;

import lombok.Data;

@Data
public class RoomAppendedEvent implements RoomDomainEvent {
  private final RoomId roomId;

  public RoomAppendedEvent(RoomId roomId) {
    this.roomId = roomId;
  }
}
