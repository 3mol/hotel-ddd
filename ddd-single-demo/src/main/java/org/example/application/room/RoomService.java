package org.example.application.room;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.example.application.BaseService;
import org.example.application.DomainEventPublisher;
import org.example.domain.order.Discount;
import org.example.domain.order.RoomId;
import org.example.domain.room.Room;
import org.example.domain.room.RoomAppendedEvent;
import org.example.domain.room.RoomCard;
import org.example.domain.room.RoomRepository;
import org.example.domain.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RoomService extends BaseService {
  final RoomRepository roomRepository;

  public RoomService(DomainEventPublisher domainEventPublisher, RoomRepository roomRepository) {
    super(domainEventPublisher);
    this.roomRepository = roomRepository;
  }

  public RoomResp appendRoom(AppendRoomReq appendRoomReq) {
    final Room room =
        new Room(
            null,
            appendRoomReq.getType(),
            RoomStatus.FREE,
            appendRoomReq.getPrice(),
            appendRoomReq.getNumber());
    final Discount discount = appendRoomReq.getDiscount();
    if (discount != null) {
      room.setDiscount(discount);
    }
    roomRepository.save(room);
    domainEventPublisher.publish(new RoomAppendedEvent(new RoomId(room.getId(), room.getNumber())));
    return new RoomResp(room.getId());
  }

  @Transactional
  public RoomCard activeRoomCard(ActiveRoomCardReq activeRoomCardReq) {
    final Room room =
        roomRepository
            .findById(activeRoomCardReq.getRoomId().getId())
            .orElseThrow(() -> new RuntimeException("房间不存在！"));
    if (!room.getStatus().equals(RoomStatus.CHECKED_IN)) {
      throw new RuntimeException("房间不是入住状态！");
    }
    final String newKey = UUID.randomUUID().toString();
    room.getRoomDoor().getRoomLock().setKey(newKey);
    roomRepository.save(room);
    return new RoomCard(newKey);
  }

  public boolean openDoor(OpenDoorReq openDoorReq) {
    final Long id = openDoorReq.getRoomId().getId();
    final Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("房间不存在！"));
    return room.open(openDoorReq.getRoomCard());
  }
}
