package org.example.room;

public interface RoomRemoteService {
  RoomResp getById(Long id);
  Boolean canBeReserved(Long id);

  double getDiscountPrice(Long id);
}
