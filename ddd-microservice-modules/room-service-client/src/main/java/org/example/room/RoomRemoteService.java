package org.example.room;

import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "RoomRemoteService",
    url = "${room-service-url:http://room-service:8080}",
    path = "/rooms")
public interface RoomRemoteService {

  @PostMapping("/")
  RoomResp appendRoom(AppendRoomReq appendRoomReq);

  @PutMapping("/room-card")
  RoomCard activeRoomCard(ActiveRoomCardReq activeRoomCardReq);

  @PutMapping("/open-room")
  boolean openDoor(OpenDoorReq openDoorReq);

  @GetMapping(value = "/{id}")
  RoomResp getById(@PathVariable(value = "id") Long id);

  @GetMapping(value = "/can-be-reserved/{id}")
  Boolean canBeReserved(@PathVariable(value = "id") Long id);

  @GetMapping(value = "/discount-price/{id}")
  double getDiscountPrice(@PathVariable(value = "id") Long id, @RequestParam Date checkInTime);
}
