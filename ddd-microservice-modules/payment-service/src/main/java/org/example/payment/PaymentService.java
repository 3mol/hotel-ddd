package org.example.payment;

import cn.hutool.core.lang.Assert;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.base.CurrentDate;
import org.example.base.DomainEventPublisher;
import org.example.order.Booking;
import org.example.order.BookingRemoteService;
import org.example.order.OrderBookedEvent;
import org.example.order.OrderCancelledEvent;
import org.example.order.OrderId;
import org.example.order.OrderRemoteService;
import org.example.room.RoomId;
import org.example.room.RoomRemoteService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class PaymentService {
  final PaymentRepository paymentRepository;
  final RoomRemoteService roomRemoteService;
  final OrderRemoteService orderRemoteService;
  final BookingRemoteService bookingRemoteService;
  final PlatformPaymentGatewayFactory platformPaymentGatewayFactory;
  final CurrentDate currentDate;
  final DomainEventPublisher domainEventPublisher;

  public PaymentService(
      PaymentRepository paymentRepository,
      RoomRemoteService roomRemoteService,
      OrderRemoteService orderRemoteService,
      BookingRemoteService bookingRemoteService,
      PlatformPaymentGatewayFactory platformPaymentGatewayFactory,
      CurrentDate currentDate,
      DomainEventPublisher domainEventPublisher) {
    this.paymentRepository = paymentRepository;
    this.roomRemoteService = roomRemoteService;
    this.orderRemoteService = orderRemoteService;
    this.bookingRemoteService = bookingRemoteService;
    this.platformPaymentGatewayFactory = platformPaymentGatewayFactory;
    this.currentDate = currentDate;
    this.domainEventPublisher = domainEventPublisher;
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
        platformPaymentGatewayFactory.create(PayMethod.WECHAT.name());
    final String status =
        platformPaymentGateway.getPaymentStatusFromPlatform(req.getThirdPartySerialNumber());
    if (status == null) {
      throw new RuntimeException("该平台流水号在平台上查询失败！");
    }
    final PayStatus payStatus = PayStatus.valueOf(status);
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
    final Booking booking = bookingRemoteService.findByOrderId(orderId.getId());
    final Date checkInTime = booking.getCheckInDate();
    final RoomId roomId = event.getRoomId();
    // 创建支付信息
    final List<Payment> payments = getPaymentsForBooked(event, checkInTime);
    paymentRepository.saveAll(payments);
    log.info("save payments: {}", payments);
    log.info("handled event: {}", event);
  }

  private List<Payment> getPaymentsForBooked(OrderBookedEvent event, Date checkInTime) {
    final double roomDiscountPrice = roomRemoteService.getDiscountPrice(event.getRoomId().getId());
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

  @EventListener
  @Transactional
  public void listen(OrderCancelledEvent event) {
    // 订单取消事件，房间释放
    log.info("OrderCancelledEvent:{}", event);
    final List<Payment> payments =
        paymentRepository.findAllBySerialNumber(event.getOrderId().getNumber());
    final Booking bookingInfo = bookingRemoteService.findByOrderId(event.getOrderId().getId());
    final Date checkInDate = bookingInfo.getCheckInDate();
    // 退回所有订金
    payments.forEach(i -> i.cancel(currentDate.get(), checkInDate));
    // 查询需要退款的Payment
    final Payment refundingPayment = findRefundingPayment(payments);
    refundPaymentToCustomer(refundingPayment);
    log.info("OrderCancelledEvent done！");
  }

  private void refundPaymentToCustomer(Payment refundingPayment) {
    if (refundingPayment == null) {
      return;
    }
    final String thirdPartySerialNumber = refundingPayment.getThirdPartySerialNumber();
    // BAD IMPL: Using proxy object.
    platformPaymentGatewayFactory
        .create(PayMethod.WECHAT.name())
        .requestRefundPayment(thirdPartySerialNumber, refundingPayment.getAmount());
  }

  private Payment findRefundingPayment(List<Payment> payments) {
    final List<Payment> refundingPayments =
        payments.stream()
            .filter(i -> i.getStatus() == PayStatus.REFUNDING)
            .collect(Collectors.toList());
    if (refundingPayments.isEmpty()) {
      return null;
    }
    // 一定是只有一个订金是处于 退款中 的状态
    Assert.isTrue(refundingPayments.size() == 1);
    return refundingPayments.get(0);
  }

  @EventListener
  @Transactional
  public void listen(PaymentRefundedEvent event) {
    // 第三方平台退款成功事件，修改支付状态
    log.info("PaymentRefundedEvent:{}", event);
    final List<Payment> payments =
        paymentRepository.findAllBySerialNumber(event.getThirdPartySerialNumber());
    payments.stream()
        .filter(i -> i.getStatus() == PayStatus.REFUNDING)
        .forEach(i -> i.setStatus(PayStatus.REFUNDED));
    log.info("PaymentRefundedEvent done！");
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
