package org.example.application.room;

import lombok.Data;
import org.example.domain.order.RoomId;

@Data
public class ActiveRoomCardReq {
  private RoomId roomId;
}
