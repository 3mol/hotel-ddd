package org.example.application.order;

import lombok.Data;
import org.example.domain.order.Order;
import org.example.domain.order.OrderStatus;
import org.example.domain.order.RoomId;

@Data
public class OrderResp {
  private Long id;
  private String number; // 订单号;
  private RoomId roomId; // 房间;
  private OrderStatus status;

  public static OrderResp of(Order order) {
    final OrderResp orderResp = new OrderResp();
    orderResp.setId(order.getId());
    orderResp.setNumber(order.getNumber());
    orderResp.setRoomId(order.getRoomId());
    orderResp.setStatus(order.getStatus());
    return orderResp;
  }
}
