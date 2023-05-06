package org.example.room;

import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.base.DomainEventPublisher;
import org.example.base.Discount;
import org.example.order.OrderCancelledEvent;
import org.example.order.OrderCheckedInEvent;
import org.example.order.OrderCheckedOutEvent;
import org.example.order.OrderRemoteService;
import org.example.order.OrderResp;
import org.example.payment.PaymentReceivedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class RoomService {
  final RoomRepository roomRepository;
  @Resource OrderRemoteService orderRemoteService;
  @Resource DomainEventPublisher domainEventPublisher;

  public RoomService(DomainEventPublisher domainEventPublisher, RoomRepository roomRepository) {
    this.domainEventPublisher = domainEventPublisher;
    this.roomRepository = roomRepository;
  }

  public RoomResp appendRoom(AppendRoomReq appendRoomReq) {
    final Room room =
        new Room(
            null,
            appendRoomReq.getType(),
            RoomStatus.FREE,
            appendRoomReq.getPrice(),
            appendRoomReq.getNumber());
    final Discount discount = appendRoomReq.getDiscount();
    if (discount != null) {
      room.setDiscount(discount);
    }
    roomRepository.save(room);
    domainEventPublisher.publish(new RoomAppendedEvent(new RoomId(room.getId(), room.getNumber())));
    return new RoomResp(room.getId(), room.getNumber());
  }

  @Transactional
  public RoomCard activeRoomCard(ActiveRoomCardReq activeRoomCardReq) {
    final Room room =
        roomRepository
            .findById(activeRoomCardReq.getRoomId().getId())
            .orElseThrow(() -> new RuntimeException("房间不存在！"));
    if (!room.getStatus().equals(RoomStatus.CHECKED_IN)) {
      throw new RuntimeException("房间不是入住状态！");
    }
    final String newKey = UUID.randomUUID().toString();
    room.getRoomDoor().getRoomLock().setKey(newKey);
    roomRepository.save(room);
    return new RoomCard(newKey);
  }

  public boolean openDoor(OpenDoorReq openDoorReq) {
    final Long id = openDoorReq.getRoomId().getId();
    final Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("房间不存在！"));
    return room.open(openDoorReq.getRoomCard());
  }

  @EventListener
  @Transactional
  public void listener(OrderCheckedInEvent event) {
    log.info("房间入住事件监听:{}", event);
    final RoomId roomId = event.getRoomId();
    final Room room =
        roomRepository.findById(roomId.getId()).orElseThrow(() -> new RuntimeException("房间不存在！"));
    room.setStatus(RoomStatus.CHECKED_IN);
    roomRepository.save(room);
    log.info("房间状态更新成功！{}", RoomStatus.CHECKED_IN);
  }

  @EventListener
  @Transactional
  public void listener(OrderCancelledEvent event) {
    // 订单取消事件，房间释放
    log.info("OrderCancelledEvent:{}", event);
    final RoomId roomId = event.getRoomId();
    final Room room =
        roomRepository.findById(roomId.getId()).orElseThrow(() -> new RuntimeException("房间不存在！"));
    room.setStatus(RoomStatus.FREE);
    roomRepository.save(room);
    log.info("OrderCancelledEvent done！{}", RoomStatus.FREE);
  }

  @EventListener
  @Transactional
  public void listener(OrderCheckedOutEvent event) {
    log.info("房间退房事件监听:{}", event);
    final RoomId roomId = event.getRoomId();
    final Room room =
        roomRepository.findById(roomId.getId()).orElseThrow(() -> new RuntimeException("房间不存在！"));
    room.setStatus(RoomStatus.CHECKED_OUT);
    roomRepository.save(room);
    log.info("房间状态更新成功！{}", RoomStatus.CHECKED_IN);
  }

  @Async
  @Transactional
  @TransactionalEventListener
  public void listen(PaymentReceivedEvent event) {
    log.info("接受到支付被接受事件：{}", event);
    final String orderNumber = event.getSerialNumber();
    final OrderResp order = orderRemoteService.findByNumber(orderNumber);
    if (order != null) {
      // 检查房间是否被预定
      final Room room = getById(order.getRoomId().getId());
      if (room.couldBeReserved()) {
        room.setStatus(RoomStatus.RESERVED);
        save(room);
      }
    }
    log.info("支付被接受事件处理完成：{}", event);
  }

  public Room getById(Long id) {
    return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("房间不存在"));
  }

  public void save(Room room) {
    roomRepository.save(room);
  }
}
