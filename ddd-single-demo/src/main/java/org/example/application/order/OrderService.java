package org.example.application.order;

import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.application.DomainEventPublisher;
import org.example.domain.order.BookingRepository;
import org.example.domain.order.Order;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderRepository;
import org.example.domain.order.OrderStatus;
import org.example.domain.payment.PaymentReceivedEvent;
import org.example.domain.room.Room;
import org.example.domain.room.RoomRepository;
import org.example.domain.room.RoomStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class OrderService {
  @Resource OrderRepository orderRepository;
  @Resource BookingRepository bookingRepository;
  @Resource RoomRepository roomRepository;

  @Resource DomainEventPublisher domainEventPublisher;

  @Transactional
  public OrderResp booking(BookingReq bookingReq) {
    final Order order = new Order();
    order.setId(null);
    order.setNumber(UUID.randomUUID().toString());
    order.setRoomId(bookingReq.getRoomId());
    order.setStatus(OrderStatus.PENDING);
    //    order.setCustomers(Lists.newArrayList());
    order.setCheckInTime(null);
    order.setCheckOutTime(null);

    final Booking booking = new Booking();
    booking.setRoomId(bookingReq.getRoomId());
    booking.setCustomer(bookingReq.getCustomer());
    booking.setPhone(bookingReq.getPhone());
    booking.setOrderedUserId(bookingReq.getUserId());
    booking.setCheckInDate(new Date());

    // todo 检查room是否已经可被预定

    // 持久化聚合
    orderRepository.save(order);
    bookingRepository.save(booking);

    final OrderResp orderResp = new OrderResp();
    orderResp.setId(order.getId());
    orderResp.setNumber(order.getNumber());
    orderResp.setRoomId(order.getRoomId());
    // 发布预定事件
    final OrderBookedEvent orderBookedEvent =
        new OrderBookedEvent(new OrderId(order.getId(), order.getNumber()), order.getRoomId());
    domainEventPublisher.publish(orderBookedEvent);
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
      final Room room =
          roomRepository
              .findById(order.getRoomId().getId())
              .orElseThrow(() -> new RuntimeException("房间不存在"));
      if (room.couldBeReserved()) {
        // todo 开启任务为预定失败的订单退款
        order.setStatus(OrderStatus.RESERVED_FAIL);
        orderRepository.save(order);
      } else {
        room.setStatus(RoomStatus.RESERVED);
        order.setStatus(OrderStatus.RESERVED);
        roomRepository.save(room);
        orderRepository.save(order);
      }
    }
    log.info("支付被接受事件处理完成：{}", event);
  }
}
