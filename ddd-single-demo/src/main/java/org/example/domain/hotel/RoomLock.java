package org.example.domain.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomLock {
  private String roomNumber;
  private String key;
  private RoomLockStatus status;
}
