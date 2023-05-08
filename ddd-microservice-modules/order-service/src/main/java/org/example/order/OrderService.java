package org.example.order;

import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.base.DomainEventPublisher;
import org.example.payment.PaymentReceivedEvent;
import org.example.payment.PaymentRemoteService;
import org.example.room.RoomRemoteService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class OrderService {
  @Resource OrderRepository orderRepository;
  @Resource BookingRepository bookingRepository;
  @Resource DomainEventPublisher domainEventPublisher;
  @Resource PaymentRemoteService paymentService;
  @Resource RoomRemoteService roomService;

  public OrderService(
      OrderRepository orderRepository,
      BookingRepository bookingRepository,
      DomainEventPublisher domainEventPublisher,
      PaymentRemoteService paymentService,
      RoomRemoteService roomService) {
    this.orderRepository = orderRepository;
    this.bookingRepository = bookingRepository;
    this.domainEventPublisher = domainEventPublisher;
    this.paymentService = paymentService;
    this.roomService = roomService;
  }

  @Transactional
  public OrderResp booking(BookingReq bookingReq) {
    if (!roomService.canBeReserved(bookingReq.getRoomId().getId())) {
      throw new RuntimeException("该房间暂时不可被预定！");
    }
    final Order order = new Order();
    order.setId(null);
    order.setNumber(UUID.randomUUID().toString());
    order.setRoomId(bookingReq.getRoomId());
    order.setStatus(OrderStatus.PENDING);
    //    order.setCustomers(Lists.newArrayList());
    order.setCheckInTime(null);
    order.setCheckOutTime(null);
    orderRepository.save(order);
    final Booking booking = new Booking();
    booking.setOrderId(new OrderId(order.getId(), order.getNumber()));
    booking.setRoomId(bookingReq.getRoomId());
    booking.setCustomer(bookingReq.getCustomer());
    booking.setPhone(bookingReq.getPhone());
    booking.setOrderedUserId(bookingReq.getUserId());
    booking.setCheckInDate(bookingReq.getCheckInDate());
    // 持久化聚合
    bookingRepository.save(booking);
    // 发布预定事件
    final OrderBookedEvent orderBookedEvent =
        new OrderBookedEvent(new OrderId(order.getId(), order.getNumber()), order.getRoomId());
    domainEventPublisher.publish(orderBookedEvent);
    return of(order);
  }

  public OrderResp of(Order order) {
    final OrderResp orderResp = new OrderResp();
    orderResp.setId(order.getId());
    orderResp.setNumber(order.getNumber());
    orderResp.setRoomId(order.getRoomId());
    orderResp.setStatus(order.getStatus());
    return orderResp;
  }

  @Transactional
  public OrderResp cancel(CancelReq cancelReq) {
    final OrderId orderId = cancelReq.getOrderId();
    final Order order = orderRepository.getOne(orderId.getId());
    order.cancel();
    // 发布事件
    final OrderCancelledEvent orderCancelledEvent =
        new OrderCancelledEvent(new OrderId(order.getId(), order.getNumber()), order.getRoomId());
    domainEventPublisher.publish(orderCancelledEvent);
    return of(order);
  }

  @Transactional
  public OrderResp checkIn(CheckInReq req) {
    final Long id = req.getOrderId().getId();
    final Order order =
        orderRepository.findById(id).orElseThrow(() -> new RuntimeException("订单不存在"));
    if (paymentService.hasUnpaidPayment(order.getNumber())) {
      throw new RuntimeException("订单未支付!");
    }
    order.checkIn(req.getCustomer(), req.getPhone());
    // 持久化聚合
    orderRepository.save(order);
    final OrderResp orderResp = new OrderResp();
    orderResp.setId(order.getId());
    orderResp.setNumber(order.getNumber());
    orderResp.setRoomId(order.getRoomId());
    orderResp.setStatus(order.getStatus());
    // 发布入住事件
    final OrderCheckedInEvent orderCheckedInEvent =
        new OrderCheckedInEvent(new OrderId(order.getId(), order.getNumber()), order.getRoomId());
    domainEventPublisher.publish(orderCheckedInEvent);
    return orderResp;
  }

  @Transactional
  public OrderResp checkOut(CheckOutReq req) {
    final Long id = req.getOrderId().getId();
    final Order order =
        orderRepository.findById(id).orElseThrow(() -> new RuntimeException("订单不存在"));
    order.checkOut();
    // 持久化聚合
    orderRepository.save(order);
    final OrderResp orderResp = new OrderResp();
    orderResp.setId(order.getId());
    orderResp.setNumber(order.getNumber());
    orderResp.setRoomId(order.getRoomId());
    orderResp.setStatus(order.getStatus());
    // 发布退房事件
    final OrderCheckedOutEvent orderCheckedOutEvent =
        new OrderCheckedOutEvent(new OrderId(order.getId(), order.getNumber()), order.getRoomId());
    domainEventPublisher.publish(orderCheckedOutEvent);
    return orderResp;
  }

  @Async
  @Transactional
  @TransactionalEventListener
  public void listen(PaymentReceivedEvent event) {
    log.info("接受到支付被接受事件：{}", event);
    final String orderNumber = event.getSerialNumber();
    final Order order = orderRepository.findByNumber(orderNumber);
    if (order != null) {
      // 检查房间是否被预定
      final Boolean canBeReserved = roomService.canBeReserved(order.getRoomId().getId());
      if (canBeReserved) {
        order.setStatus(OrderStatus.RESERVED);
        orderRepository.save(order);
      } else {
        // todo 开启任务为预定失败的订单退款
        order.setStatus(OrderStatus.RESERVED_FAIL);
        orderRepository.save(order);
      }
    }
    log.info("支付被接受事件处理完成：{}", event);
  }

  public OrderResp findByNumber(String orderNumber) {
    final Order order = orderRepository.findByNumber(orderNumber);
    return of(order);
  }

  public OrderResp getById(Long orderId) {
    final Order order = orderRepository.getOne(orderId);
    return of(order);
  }
}
