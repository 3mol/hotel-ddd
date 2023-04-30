package org.example.application.payment;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import org.example.application.DomainEventPublisher;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.order.OrderId;
import org.example.domain.order.PayMethod;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;
import org.example.domain.order.RoomId;
import org.example.domain.payment.Payment;
import org.example.domain.payment.PaymentId;
import org.example.domain.payment.PaymentReceivedEvent;
import org.example.domain.payment.PaymentRepository;
import org.example.domain.room.Room;
import org.example.domain.room.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
  @Mock
  DomainEventPublisher domainEventPublisher;
  @Mock
  PaymentRepository paymentRepository;
  @Mock
  RoomRepository roomRepository;
  @InjectMocks
  PaymentService paymentService;

  @Test
  void payForBooking() {
    // given
    final Payment payment = getPayment();
    when(paymentRepository.findById(any())).thenReturn(Optional.of(payment));
    final BookingPaymentReq req = new BookingPaymentReq();
    req.setPaymentId(new PaymentId(payment.getId(), payment.getSerialNumber()));
    req.setPayMethod(PayMethod.WECHAT);
    req.setThirdPartySerialNumber("ThirdPartySerialNumber");
    // when
    paymentService.payForBooking(req);
    // then
    assertEquals(PayStatus.PAID, payment.getStatus());
    verify(domainEventPublisher, new Times(1)).publish(any(PaymentReceivedEvent.class));
  }

  private static Payment getPayment() {
    final Payment payment = new Payment();
    payment.setId(1L);
    payment.setMethod(null);
    payment.setSerialNumber("OrderNumber");
    payment.setThirdPartySerialNumber(null);
    payment.setType(PayType.DEPOSIT);
    payment.setStatus(PayStatus.UNPAID);
    payment.setAmount(10.0D);
    payment.setCreatedAt(new Date());
    payment.setPaidAt(null);
    return payment;
  }

  @Test
  void listen() {
    final Room mockRoom = mock(Room.class);
    when(roomRepository.findById(any())).thenReturn(Optional.of(mockRoom));
    when(mockRoom.getPrice()).thenReturn(100D);
    paymentService.listen(
       new OrderBookedEvent(new OrderId(1L, "OrderNumber"), new RoomId(1L, "401")));
    // 验证是否执行了1次saveAll方法, 保存时金额是20元\80\30元,分别是订金\尾款\押金
    verify(paymentRepository, times(1)).saveAll(any());
    ArgumentCaptor<List<Payment>> argument = ArgumentCaptor.forClass(List.class);
    verify(paymentRepository).saveAll(argument.capture());
    assertEquals(20, argument.getValue().get(0).getAmount());
    assertEquals(80, argument.getValue().get(1).getAmount());
    assertEquals(30, argument.getValue().get(2).getAmount());
  }
}
