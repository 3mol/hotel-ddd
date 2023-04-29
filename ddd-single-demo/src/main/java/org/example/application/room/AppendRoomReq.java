package org.example.application.room;

import lombok.Data;
import org.example.domain.order.Discount;
import org.example.domain.room.RoomType;

@Data
public class AppendRoomReq {
  private RoomType type; // 类型 [单人间、双人间]
  private Double price; // 价格
  private String number; // 房间号
  private Discount discount; // 折扣
}
