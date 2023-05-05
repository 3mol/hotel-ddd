package org.example.room;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class RoomLock {
  private String key;
  private RoomLockStatus roomLockStatus;

  public RoomLock() {}
}
