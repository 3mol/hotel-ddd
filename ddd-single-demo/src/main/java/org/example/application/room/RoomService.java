package org.example.application.room;

import lombok.extern.slf4j.Slf4j;
import org.example.application.BaseService;
import org.example.application.DomainEventPublisher;
import org.example.domain.order.Discount;
import org.example.domain.order.RoomId;
import org.example.domain.room.Room;
import org.example.domain.room.RoomAppendedEvent;
import org.example.domain.room.RoomRepository;
import org.example.domain.room.RoomStatus;
import org.springframework.stereotype.Service;

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
}
