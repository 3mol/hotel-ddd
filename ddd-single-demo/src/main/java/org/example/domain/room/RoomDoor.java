package org.example.domain.room;

import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RoomDoor {
  private String roomNumber;
  private RoomDoorStatus roomDoorStatus;
  private RoomLock roomLock;

  public RoomDoor(String roomNumber) {
    this(roomNumber, RoomDoorStatus.CLOSE, new RoomLock("", RoomLockStatus.LOCKED));
  }

  public RoomDoor(String roomNumber, RoomDoorStatus roomDoorStatus, RoomLock roomLock) {
    this.roomNumber = roomNumber;
    this.roomDoorStatus = roomDoorStatus;
    this.roomLock = roomLock;
  }

  public RoomDoor() {}
}
