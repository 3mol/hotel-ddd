package org.example.api;

import javax.annotation.Resource;
import org.example.room.ActiveRoomCardReq;
import org.example.room.AppendRoomReq;
import org.example.room.OpenDoorReq;
import org.example.room.RoomCard;
import org.example.room.RoomRemoteService;
import org.example.room.RoomResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class RoomApi {
  @Resource RoomRemoteService remoteService;

  @PostMapping("/")
  RoomResp appendRoom(AppendRoomReq appendRoomReq) {
    return remoteService.appendRoom(appendRoomReq);
  }

  @PutMapping("/room-card")
  RoomCard activeRoomCard(ActiveRoomCardReq activeRoomCardReq) {
    return remoteService.activeRoomCard(activeRoomCardReq);
  }

  @PutMapping("/open-room")
  boolean openDoor(OpenDoorReq openDoorReq) {
    return remoteService.openDoor(openDoorReq);
  }

  @GetMapping(value = "/{id}")
  RoomResp getById(@PathVariable(value = "id") Long id) {
    return remoteService.getById(id);
  }
}
