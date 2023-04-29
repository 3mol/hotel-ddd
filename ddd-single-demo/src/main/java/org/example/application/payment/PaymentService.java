package org.example.application.payment;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.example.application.BaseService;
import org.example.application.DomainEventPublisher;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;
import org.example.domain.order.RoomId;
import org.example.domain.payment.Payment;
import org.example.domain.payment.PaymentReceivedEvent;
import org.example.domain.payment.PaymentRepository;
import org.example.domain.room.Room;
import org.example.domain.room.RoomRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class PaymentService extends BaseService {
  final PaymentRepository paymentRepository;
  final RoomRepository roomRepository;

  public PaymentService(
      DomainEventPublisher domainEventPublisher,
      PaymentRepository paymentRepository,
      RoomRepository roomRepository) {
    super(domainEventPublisher);
    this.paymentRepository = paymentRepository;
    this.roomRepository = roomRepository;
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
    final PaymentReceivedEvent paymentReceivedEvent =
        payment.receivePay(req.getPayMethod(), req.getThirdPartySerialNumber());
    paymentRepository.save(payment);
    // todo 发送支付成功事件
    domainEventPublisher.publish(paymentReceivedEvent);
  }

  @Async
  @TransactionalEventListener
  public void listen(OrderBookedEvent event) {
    log.info("handling event: {}", event);
    final RoomId roomId = event.getRoomId();
    final Room room =
        roomRepository.findById(roomId.getId()).orElseThrow(() -> new RuntimeException("房间不存在！"));
    // 创建支付信息
    final Payment payment = new Payment();
    payment.setMethod(null);
    payment.setSerialNumber(event.getOrderId().getNumber());
    payment.setType(PayType.DEPOSIT);
    payment.setStatus(PayStatus.UNPAID);
    payment.setAmount(room.getPrice() * 0.2);
    payment.setCreatedAt(new Date());
    payment.setPaidAt(new Date());
    paymentRepository.save(payment);
    log.info("handled event: {}", event);
  }
}
