package org.example.room;

import lombok.Data;

@Data
public class OpenDoorReq {
  private RoomId roomId;
  private RoomCard roomCard;
}
