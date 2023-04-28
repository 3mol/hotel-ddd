package org.example.domain.hotel;

import lombok.Data;
import org.example.domain.order.Discount;

@Data
public class Room {
  private String id;
  private RoomType type; // 类型 [单人间、双人间]
  private RoomStatus status; // 状态 [空闲、已预定、已入住]
  private double price; // 价格
  private String number; // 房间号
  private RoomDoor roomDoor;
  private Discount discount; // 折扣
  // private Reserve reserve; // 预定
  // private CheckIn checkIn; // 登记入住

  public Room(String id, RoomType type, RoomStatus status, double price, String number) {
    this(id, type, status, price, number, null, null);
    this.roomDoor = new RoomDoor(number);
  }

  public Room(
      String id,
      RoomType type,
      RoomStatus status,
      double price,
      String number,
      RoomDoor roomDoor,
      Discount discount) {
    this.id = id;
    this.type = type;
    this.status = status;
    this.price = price;
    this.number = number;
    this.roomDoor = roomDoor;
    this.discount = discount;
  }

  public void open(RoomCard roomCard) {
    if (roomCard == null) {
      throw new RuntimeException("房卡不能为空");
    }
    if (roomCard.getKey().equals(this.getRoomDoor().getRoomLock().getKey())) {
      this.getRoomDoor().setStatus(RoomDoorStatus.OPEN);
    }
  }
}
