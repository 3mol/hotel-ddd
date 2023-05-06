package org.example.order;

import lombok.Data;
import org.example.room.RoomId;

@Data
public class OrderResp {
  private Long id;
  private String number; // 订单号;
  private RoomId roomId; // 房间;
  private OrderStatus status;

}
