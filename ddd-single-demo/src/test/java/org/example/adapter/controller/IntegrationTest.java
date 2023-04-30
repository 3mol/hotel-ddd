package org.example.adapter.controller;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.application.hotel.HotelService;
import org.example.application.order.BookingReq;
import org.example.application.order.CheckInReq;
import org.example.application.order.CheckOutReq;
import org.example.application.order.OrderResp;
import org.example.application.order.OrderService;
import org.example.application.payment.BookingPaymentReq;
import org.example.application.payment.CheckInPaymentReq;
import org.example.application.payment.PaymentService;
import org.example.application.room.ActiveRoomCardReq;
import org.example.application.room.AppendRoomReq;
import org.example.application.room.OpenDoorReq;
import org.example.application.room.RoomResp;
import org.example.application.room.RoomService;
import org.example.client.hotel.AppendHotelReq;
import org.example.domain.hotel.HotelId;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderStatus;
import org.example.domain.order.PayMethod;
import org.example.domain.order.PayType;
import org.example.domain.order.RoomId;
import org.example.domain.payment.Payment;
import org.example.domain.payment.PaymentId;
import org.example.domain.room.RoomCard;
import org.example.domain.room.RoomType;
import org.example.domain.user.Customer;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class IntegrationTest {
  @Resource RoomService roomService;
  @Resource HotelService hotelService;
  @Resource PaymentService paymentService;
  @Resource OrderService orderService;
  private RoomResp room;
  private OrderResp bookedOrder;
  private RoomCard activedRoomCard;
  private List<Payment> allUnpaidPayment;

  @BeforeEach
  void setUp() {
    // 创建酒店
    val appendHotelResp =
        hotelService.append(new AppendHotelReq("test", "address", "10086", "good hotel"));
    // 为该酒店添加房间
    room =
        roomService.appendRoom(
            new AppendRoomReq(
                new HotelId(appendHotelResp.getId(), appendHotelResp.getName()),
                RoomType.SINGLE,
                100d,
                "401",
                null));
  }

  @Test
  @DisplayName("用户预定-入住-退房主流程")
  void main() {
    // 用户预定房间
    bookingRoom();
    // 用户查询待支付的账单
    getUnpaidPayment();
    // 用户支付订金
    payForBooking();
    // 用户入住支付尾款、押金
    payForCheckIn();
    // 用户入住登记
    checkIn();
    // 服务员激活房卡
    activeRoomCard();
    // 用户开门
    enterRoom();
    // 服务员为用户退房
    checkOut();
  }

  private void activeRoomCard() {
    final ActiveRoomCardReq activeRoomCardReq = new ActiveRoomCardReq();
    activeRoomCardReq.setRoomId(new RoomId(room.getId(), room.getNumber()));
    this.activedRoomCard = roomService.activeRoomCard(activeRoomCardReq);

    Assertions.assertNotNull(activedRoomCard.getKey());
  }

  private void getUnpaidPayment() {
    this.allUnpaidPayment = paymentService.findAllUnpaidPayment(bookedOrder.getNumber());
    log.info("allUnpaidPayment: {}", allUnpaidPayment);
    Assertions.assertEquals(3, allUnpaidPayment.size());
  }

  private void checkOut() {
    final CheckOutReq checkOutReq = new CheckOutReq();
    checkOutReq.setOrderId(new OrderId(bookedOrder.getId(), bookedOrder.getNumber()));
    final OrderResp checkOutResp = orderService.checkOut(checkOutReq);
    log.info("checkOutResp: {}", checkOutResp);

    Assertions.assertEquals(OrderStatus.CHECKED_OUT, checkOutResp.getStatus());
  }

  private void enterRoom() {
    final OpenDoorReq openDoorReq = new OpenDoorReq();
    openDoorReq.setRoomCard(activedRoomCard);
    openDoorReq.setRoomId(new RoomId(room.getId(), room.getNumber()));
    final boolean openDoor = roomService.openDoor(openDoorReq);
    log.info("openDoor: {}", openDoor);
    Assertions.assertTrue(openDoor);
  }

  private void checkIn() {
    final CheckInReq checkInReq = new CheckInReq();
    checkInReq.setOrderId(new OrderId(bookedOrder.getId(), bookedOrder.getNumber()));
    checkInReq.setCustomer(Lists.newArrayList(new Customer("张三", "88888888", null)));
    checkInReq.setPhone("10086");
    final OrderResp resp = orderService.checkIn(checkInReq);
    log.info("checkInResp: {}", resp);

    Assertions.assertEquals(OrderStatus.CHECKED_IN, resp.getStatus());
  }

  private void payForCheckIn() {
    final Payment finalPayment = getPayment(PayType.FINAL_PAYMENT);
    final Payment chargePayment = getPayment(PayType.DEPOSIT_CHARGE);
    paymentService.payForCheckIn(
        new CheckInPaymentReq(
            Arrays.asList(
                new PaymentId(finalPayment.getId(), finalPayment.getSerialNumber()),
                new PaymentId(chargePayment.getId(), chargePayment.getSerialNumber())),
            PayMethod.ALIPAY,
            "ThirdPartySerialNumber"));

    Assertions.assertFalse(paymentService.hasUnpaidPayment(this.bookedOrder.getNumber()));
  }

  private void payForBooking() {
    final Payment payment = getPayment(PayType.DEPOSIT);
    final BookingPaymentReq bookingPaymentReq = new BookingPaymentReq();
    bookingPaymentReq.setPaymentId(new PaymentId(payment.getId(), payment.getSerialNumber()));
    bookingPaymentReq.setPayMethod(PayMethod.ALIPAY);
    bookingPaymentReq.setThirdPartySerialNumber("ThirdPartySerialNumber");
    paymentService.payForBooking(bookingPaymentReq);

    final List<Payment> remainPayments =
        paymentService.findAllUnpaidPayment(payment.getSerialNumber());
    Assertions.assertEquals(2, remainPayments.size());
  }

  private void bookingRoom() {
    final BookingReq bookingReq = new BookingReq();
    bookingReq.setRoomId(new RoomId(room.getId(), room.getNumber()));
    bookingReq.setCustomer(new Customer("张三", "88888888", null));
    bookingReq.setPhone("110");
    bookingReq.setCheckInDate(new Date());
    bookingReq.setUserId(new UserId(1L, "张三"));
    this.bookedOrder = orderService.booking(bookingReq);
    log.info("booking:{}", this.bookedOrder);
    // 订单的状态为 PENDING
    Assertions.assertEquals(OrderStatus.PENDING, bookedOrder.getStatus());
  }

  private Payment getPayment(PayType deposit) {
    return allUnpaidPayment.stream().filter(i -> i.getType() == deposit).findFirst().orElseThrow();
  }
}
