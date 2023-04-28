package org.example.domain.hotel;

import lombok.Data;

@Data
public class RoomDoor {
  private String roomNumber;
  private RoomDoorStatus status;
  private RoomLock roomLock;

  public RoomDoor(String roomNumber) {
    this(roomNumber, RoomDoorStatus.CLOSE, new RoomLock(roomNumber, "", RoomLockStatus.LOCKED));
  }

  public RoomDoor(String roomNumber, RoomDoorStatus status, RoomLock roomLock) {
    this.roomNumber = roomNumber;
    this.status = status;
    this.roomLock = roomLock;
  }
}
