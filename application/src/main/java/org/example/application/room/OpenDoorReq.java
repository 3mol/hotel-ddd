package org.example.application.room;

import lombok.Data;
import org.example.domain.order.RoomId;
import org.example.domain.room.RoomCard;

@Data
public class OpenDoorReq {
  private RoomId roomId;
  private RoomCard roomCard;
}
