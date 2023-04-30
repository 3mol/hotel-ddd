package org.example.application.payment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.application.DomainEventPublisher;
import org.example.application.room.ActiveRoomCardReq;
import org.example.application.room.AppendRoomReq;
import org.example.application.room.RoomService;
import org.example.domain.order.Discount;
import org.example.domain.order.RoomId;
import org.example.domain.room.Room;
import org.example.domain.room.RoomAppendedEvent;
import org.example.domain.room.RoomCard;
import org.example.domain.room.RoomRepository;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
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
}
