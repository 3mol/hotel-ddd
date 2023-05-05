package org.example.application.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.hutool.core.date.DateUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.Infra.gateway.PlatformPaymentGateway;
import org.example.Infra.gateway.PlatformPaymentGatewayFactory;
import org.example.application.DomainEventPublisher;
import org.example.common.CurrentDate;
import org.example.domain.order.Booking;
import org.example.domain.order.BookingRepository;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.order.OrderCancelledEvent;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
  @Mock DomainEventPublisher domainEventPublisher;
  @Mock PaymentRepository paymentRepository;
  @Mock RoomRepository roomRepository;
  @Mock BookingRepository bookingRepository;
  @Mock PlatformPaymentGatewayFactory platformPaymentGatewayFactory;
  @InjectMocks PaymentService paymentService;
  @Mock CurrentDate currentDate;
  @Mock PlatformPaymentGateway wechatPlatformPaymentGateway;

  @Test
  @DisplayName("预定后支付订金")
  void payForBooking() {
    // given
    when(platformPaymentGatewayFactory.create(PayMethod.WECHAT.name()))
        .thenReturn(wechatPlatformPaymentGateway);
    final Payment payment = getPayment();
    when(wechatPlatformPaymentGateway.getPaymentStatusFromPlatform(any()))
        .thenReturn(PayStatus.PAID.name());
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

  @Test
  @DisplayName("入住前支付尾款")
  void payForCheckIn() {
    // given
    final Payment payment1 = getPayment();
    payment1.setType(PayType.FINAL_PAYMENT);
    final Payment payment2 = getPayment();
    payment2.setType(PayType.DEPOSIT_CHARGE);

    when(paymentRepository.findAllById(any())).thenReturn(Arrays.asList(payment1, payment2));
    final CheckInPaymentReq req = new CheckInPaymentReq();
    req.setPaymentIds(
        Arrays.asList(
            new PaymentId(payment1.getId(), payment1.getSerialNumber()),
            new PaymentId(payment2.getId(), payment2.getSerialNumber())));
    req.setPayMethod(PayMethod.WECHAT);
    req.setThirdPartySerialNumber("ThirdPartySerialNumber");
    // when
    paymentService.payForCheckIn(req);
    // then
    assertEquals(PayStatus.PAID, payment1.getStatus());
    assertEquals(PayStatus.PAID, payment2.getStatus());
    verify(domainEventPublisher, new Times(2)).publish(any(PaymentReceivedEvent.class));
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
  @DisplayName("周日支付, 不打折")
  void listen() {
    final Room room = new Room();
    room.setPrice(100d);
    when(roomRepository.findById(any())).thenReturn(Optional.of(room));
    final Booking booking = new Booking();
    // 周日，不打价
    booking.setCheckInDate(DateUtil.parseDate("2023-04-30"));
    when(bookingRepository.findByOrderId(any())).thenReturn(booking);

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

  @Test
  @DisplayName("周五支付, 打五折")
  void listen2() {
    final Room room = new Room();
    room.setPrice(100d);
    when(roomRepository.findById(any())).thenReturn(Optional.of(room));
    final Booking booking = new Booking();
    // 周五，打5折
    booking.setCheckInDate(DateUtil.parseDate("2023-04-28"));
    when(bookingRepository.findByOrderId(any())).thenReturn(booking);

    paymentService.listen(
        new OrderBookedEvent(new OrderId(1L, "OrderNumber"), new RoomId(1L, "401")));
    // 验证是否执行了1次saveAll方法, 保存时金额是20元\80\30元,分别是订金\尾款\押金
    verify(paymentRepository, times(1)).saveAll(any());
    ArgumentCaptor<List<Payment>> argument = ArgumentCaptor.forClass(List.class);
    verify(paymentRepository).saveAll(argument.capture());
    assertEquals(100 * 0.5 * 0.2, argument.getValue().get(0).getAmount());
    assertEquals(100 * 0.5 * 0.8, argument.getValue().get(1).getAmount());
    assertEquals(30, argument.getValue().get(2).getAmount());
  }

  @Test
  @DisplayName("预定并且支付成功后，在24小时之前退款，退款成功")
  void listenOrderCancelledEvent1() {
    when(platformPaymentGatewayFactory.create(PayMethod.WECHAT.name()))
        .thenReturn(wechatPlatformPaymentGateway);
    final Payment payment = new Payment();
    payment.setId(1L);
    payment.setMethod(null);
    payment.setSerialNumber("OrderNumber");
    payment.setThirdPartySerialNumber("ThirdPartySerialNumber");
    payment.setType(PayType.DEPOSIT);
    payment.setStatus(PayStatus.PAID);
    payment.setAmount(10.0D);
    payment.setCreatedAt(DateUtil.parseDate("2023-05-01"));
    payment.setPaidAt(DateUtil.parseDate("2023-05-01"));
    final Booking booking = new Booking();
    booking.setCheckInDate(DateUtil.parseDate("2023-05-03"));
    when(currentDate.get()).thenReturn(DateUtil.parseDate("2023-05-01"));
    when(bookingRepository.findByOrderId(any())).thenReturn(booking);
    when(paymentRepository.findAllBySerialNumber(any())).thenReturn(List.of(payment));
    final OrderId orderNumber = new OrderId(1L, "OrderNumber");
    final RoomId roomId = new RoomId(1L, "401");
    paymentService.listen(new OrderCancelledEvent(orderNumber, roomId));
    verify(platformPaymentGatewayFactory, times(1)).create(any());
    verify(wechatPlatformPaymentGateway, times(1))
        .requestRefundPayment(any(String.class), any(Double.class));
  }

  @Test
  @DisplayName("预定并且支付成功后，在24小时之内退款，不会进行退款")
  void listenOrderCancelledEvent2() {
    when(currentDate.get()).thenReturn(DateUtil.parseDate("2023-05-01"));
    final Payment payment = new Payment();
    payment.setId(1L);
    payment.setMethod(null);
    payment.setSerialNumber("OrderNumber");
    payment.setThirdPartySerialNumber("ThirdPartySerialNumber");
    payment.setType(PayType.DEPOSIT);
    payment.setStatus(PayStatus.PAID);
    payment.setAmount(10.0D);
    payment.setCreatedAt(DateUtil.parseDate("2023-05-01"));
    payment.setPaidAt(DateUtil.parseDate("2023-05-01"));
    final Booking booking = new Booking();
    booking.setCheckInDate(DateUtil.parseDate("2023-05-02"));
    when(bookingRepository.findByOrderId(any())).thenReturn(booking);
    when(paymentRepository.findAllBySerialNumber(any())).thenReturn(List.of(payment));
    final OrderId orderNumber = new OrderId(1L, "OrderNumber");
    final RoomId roomId = new RoomId(1L, "401");
    paymentService.listen(new OrderCancelledEvent(orderNumber, roomId));
    verify(platformPaymentGatewayFactory, never()).create(any());
  }
}
