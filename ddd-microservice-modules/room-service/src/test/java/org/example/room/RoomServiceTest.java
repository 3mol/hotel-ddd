package org.example.room;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.base.Discount;
import org.example.base.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
  @Mock DomainEventPublisher domainEventPublisher;
  @Mock RoomRepository roomRepository;
  @InjectMocks RoomService roomService;

  @Test
  void appendRoom() {
    // given

    // when
    final AppendRoomReq appendRoomReq = new AppendRoomReq();
    appendRoomReq.setType(RoomType.SINGLE);
    appendRoomReq.setPrice(100.0D);
    appendRoomReq.setNumber("401");
    final Discount discount = new Discount();
    discount.setDiscount(0.8D);
    discount.setDiscountName("工作日折扣");
    appendRoomReq.setDiscount(discount);

    roomService.appendRoom(appendRoomReq);
    // then
    verify(domainEventPublisher, times(1)).publish(any(RoomAppendedEvent.class));
  }

  @Test
  void activeRoomCard() {
    // given
    final Room room = new Room(1L, RoomType.SINGLE, RoomStatus.CHECKED_IN, 100, "401");
    room.setStatus(RoomStatus.CHECKED_IN);

    when(roomRepository.findById(any())).thenReturn(Optional.of(room));
    final ActiveRoomCardReq activeRoomCardReq = new ActiveRoomCardReq();
    activeRoomCardReq.setRoomId(new RoomId(1L, "401"));
    // when
    final RoomCard roomCard = roomService.activeRoomCard(activeRoomCardReq);
    // then
    assertNotNull(roomCard.getKey());
  }

  @Test
  void openRoomDoor() {
    // given
    final Room room = new Room(1L, RoomType.SINGLE, RoomStatus.CHECKED_IN, 100, "401");
    room.setStatus(RoomStatus.CHECKED_IN);
    room.getRoomDoor().getRoomLock().setKey("1234567890");
    when(roomRepository.findById(any())).thenReturn(Optional.of(room));
    final ActiveRoomCardReq activeRoomCardReq = new ActiveRoomCardReq();
    activeRoomCardReq.setRoomId(new RoomId(1L, "401"));
    // when
    final OpenDoorReq openDoorReq = new OpenDoorReq();
    openDoorReq.setRoomCard(new RoomCard("1234567890"));
    openDoorReq.setRoomId(new RoomId(1L, "401"));

    boolean open = roomService.openDoor(openDoorReq);
    // then
    assertTrue(open);
  }
}
