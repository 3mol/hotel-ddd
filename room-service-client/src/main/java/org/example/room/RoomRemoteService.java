package org.example.room;

import java.util.Optional;

public interface RoomRemoteService {
  RoomResp getById(Long id);
  Boolean canBeReserved(Long id);

  double getDiscountPrice(Long id);
}
