package org.example.domain.room;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.example.domain.order.Discount;
import org.example.domain.order.OrderId;

@Data
@Entity
public class Room {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private Long id;

  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "order_id")),
    @AttributeOverride(name = "number", column = @Column(name = "order_number"))
  })
  @Embedded
  private OrderId orderId;

  private RoomType type; // 类型 [单人间、双人间]
  private RoomStatus status; // 状态 [空闲、已预定、已入住]
  private Double price; // 价格
  private String number; // 房间号
  private RoomDoor roomDoor;
  private Discount discount; // 折扣

  public Room(Long id, RoomType type, RoomStatus status, double price, String number) {
    this(id, type, status, price, number, null, null);
    this.roomDoor = new RoomDoor(number);
  }

  public Room(
      Long id,
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

  public Room() {}

  public void open(RoomCard roomCard) {
    if (roomCard == null) {
      throw new RuntimeException("房卡不能为空");
    }
    if (roomCard.getKey().equals(this.getRoomDoor().getRoomLock().getKey())) {
      this.getRoomDoor().setRoomDoorStatus(RoomDoorStatus.OPEN);
    }
  }

  public boolean couldBeReserved() {
    return getStatus() != RoomStatus.FREE;
  }
}
