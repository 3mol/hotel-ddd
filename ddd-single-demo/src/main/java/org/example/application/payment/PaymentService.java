package org.example.application.payment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.application.BaseService;
import org.example.application.DomainEventPublisher;
import org.example.application.order.Booking;
import org.example.domain.order.BookingRepository;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.order.OrderCancelledEvent;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderRepository;
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
import org.example.infrastructure.gateway.PlatformPaymentGateway;
import org.example.infrastructure.gateway.PlatformPaymentGatewayFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class PaymentService extends BaseService {
  final PaymentRepository paymentRepository;
  final RoomRepository roomRepository;
  final OrderRepository orderRepository;
  final BookingRepository bookingRepository;
  final PlatformPaymentGatewayFactory platformPaymentGatewayFactory;

  public PaymentService(
      DomainEventPublisher domainEventPublisher,
      PaymentRepository paymentRepository,
      RoomRepository roomRepository,
      OrderRepository orderRepository,
      BookingRepository bookingRepository,
      PlatformPaymentGatewayFactory platformPaymentGatewayFactory) {
    super(domainEventPublisher);
    this.paymentRepository = paymentRepository;
    this.roomRepository = roomRepository;
    this.orderRepository = orderRepository;
    this.bookingRepository = bookingRepository;
    this.platformPaymentGatewayFactory = platformPaymentGatewayFactory;
  }

  @Transactional
  public void payForBooking(BookingPaymentReq req) {
    Payment payment =
        paymentRepository
            .findById(req.getPaymentId().getId())
            .orElseThrow(() -> new RuntimeException("支付信息不存在！"));
    if (payment.getStatus().equals(PayStatus.PAID)) {
      throw new RuntimeException("该订单已经支付！");
    }
    // todo 校验第三方支付平台，是否存在该流水是否已经支付
    final PlatformPaymentGateway<?, ?> platformPaymentGateway =
        platformPaymentGatewayFactory.create(PayMethod.WECHAT);
    final PayStatus payStatus =
        platformPaymentGateway.getPaymentStatusFromPlatform(req.getThirdPartySerialNumber());
    if (payStatus == null) {
      throw new RuntimeException("该平台流水号在平台上查询失败！");
    }
    if (payStatus != PayStatus.PAID) {
      throw new RuntimeException("该订单未支付成功！");
    }
    final PaymentReceivedEvent paymentReceivedEvent =
        payment.receivePay(req.getPayMethod(), req.getThirdPartySerialNumber());
    paymentRepository.save(payment);
    // todo 发送支付成功事件
    domainEventPublisher.publish(paymentReceivedEvent);
  }

  @Async // mark 不知道为什么删除Async后事务不会提交
  @TransactionalEventListener
  public void listen(OrderBookedEvent event) {
    log.info("handling event: {}", event);
    final OrderId orderId = event.getOrderId();
    final Booking booking = bookingRepository.findByOrderId(orderId);
    final Date checkInTime = booking.getCheckInDate();
    final RoomId roomId = event.getRoomId();
    final Room room =
        roomRepository.findById(roomId.getId()).orElseThrow(() -> new RuntimeException("房间不存在！"));
    // 创建支付信息
    final List<Payment> payments = getPaymentsForBooked(event, room, checkInTime);
    paymentRepository.saveAll(payments);
    log.info("save payments: {}", payments);
    log.info("handled event: {}", event);
  }

  @EventListener
  @Transactional
  public void listener(OrderCancelledEvent event) {
    // 订单取消事件，房间释放
    log.info("OrderCancelledEvent:{}", event);
    final List<Payment> payments =
        paymentRepository.findAllBySerialNumber(event.getOrderId().getNumber());
    payments.forEach(Payment::cancel);
    // todo 调用接口进行退款
    log.info("OrderCancelledEvent done！");
  }

  @EventListener
  @Transactional
  public void listener(PaymentRefundedEvent event) {
    // 第三方平台退款成功事件，修改支付状态
    log.info("PaymentRefundedEvent:{}", event);
    final List<Payment> payments =
        paymentRepository.findAllBySerialNumber(event.getThirdPartySerialNumber());
    payments.stream()
        .filter(i -> i.getStatus() == PayStatus.REFUNDING)
        .forEach(i -> i.setStatus(PayStatus.REFUNDED));
    log.info("PaymentRefundedEvent done！");
  }

  private List<Payment> getPaymentsForBooked(OrderBookedEvent event, Room room, Date checkInTime) {
    final double roomDiscountPrice = room.getDiscountPrice(checkInTime);
    // 尾款支付\押金支付
    return Arrays.asList(
        buildUnpaid(PayType.DEPOSIT, event.getOrderId().getNumber(), roomDiscountPrice * 0.2),
        buildUnpaid(PayType.FINAL_PAYMENT, event.getOrderId().getNumber(), roomDiscountPrice * 0.8),
        buildUnpaid(PayType.DEPOSIT_CHARGE, event.getOrderId().getNumber(), 30d));
  }

  private Payment buildUnpaid(PayType payType, String serialNumber, Double amount) {
    final Payment payment = new Payment();
    payment.setSerialNumber(serialNumber);
    payment.setType(payType);
    payment.setStatus(PayStatus.UNPAID);
    payment.setAmount(amount);
    payment.setCreatedAt(new Date());
    return payment;
  }

  /**
   * 同时支付尾款和押金
   *
   * @param req ~
   */
  public void payForCheckIn(CheckInPaymentReq req) {
    final List<Payment> paymentList =
        paymentRepository.findAllById(
            req.getPaymentIds().stream().map(PaymentId::getId).collect(Collectors.toList()));

    if (paymentList.stream().allMatch(i -> i.getStatus() == PayStatus.PAID)) {
      throw new RuntimeException("该订单已经支付！");
    }
    if (paymentList.stream().anyMatch(i -> i.getStatus() == PayStatus.PAID)) {
      throw new RuntimeException("该订单已部分支付！");
    }
    // todo 校验第三方支付平台，是否存在该流水是否已经支付
    final List<PaymentReceivedEvent> paymentReceivedEvents =
        paymentList.stream()
            .map(i -> i.receivePay(req.getPayMethod(), req.getThirdPartySerialNumber()))
            .collect(Collectors.toList());
    paymentRepository.saveAll(paymentList);
    // 发送支付成功事件
    paymentReceivedEvents.forEach(e -> domainEventPublisher.publish(e));
  }

  public boolean hasUnpaidPayment(String number) {
    final List<Payment> payments = paymentRepository.findAllBySerialNumber(number);
    if (payments.isEmpty()) {
      throw new RuntimeException("支付信息不存在！");
    }
    return payments.stream().anyMatch(i -> i.getStatus() == PayStatus.UNPAID);
  }

  public List<Payment> findAllUnpaidPayment(String number) {
    return paymentRepository.findAllBySerialNumber(number).stream()
        .filter(i -> i.getStatus() == PayStatus.UNPAID)
        .collect(Collectors.toList());
  }
}
