package org.example.application.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.example.application.DomainEventPublisher;
import org.example.application.room.AppendRoomReq;
import org.example.application.room.RoomService;
import org.example.domain.order.Discount;
import org.example.domain.room.RoomAppendedEvent;
import org.example.domain.room.RoomRepository;
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
  void payForBooking() {
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
}
