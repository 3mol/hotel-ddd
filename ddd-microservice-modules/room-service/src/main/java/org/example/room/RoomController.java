package org.example.room;

import java.util.Date;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {
  @Resource RoomService roomService;

  @PostMapping("/")
  public RoomResp appendRoom(AppendRoomReq appendRoomReq) {
    return roomService.appendRoom(appendRoomReq);
  }

  @PutMapping("/room-card")
  public RoomCard activeRoomCard(ActiveRoomCardReq activeRoomCardReq) {
    return roomService.activeRoomCard(activeRoomCardReq);
  }

  @PutMapping("/open-room")
  public boolean openDoor(OpenDoorReq openDoorReq) {
    return roomService.openDoor(openDoorReq);
  }

  @GetMapping(value = "/{id}")
  public RoomResp getById(@PathVariable(value = "id") Long id) {
    final Room room = roomService.getById(id);
    return new RoomResp(room.getId(), room.getNumber());
  }

  @GetMapping(value = "/can-be-reserved/{id}")
  public Boolean canBeReserved(@PathVariable(value = "id") Long id) {
    return roomService.canBeReserved(id);
  }

  @GetMapping(value = "/discount-price/{id}")
  public double getDiscountPrice(
      @PathVariable(value = "id") Long id, @RequestParam Date checkInTime) {
    return roomService.getDiscountPrice(id, checkInTime);
  }
}
