package org.example.room;

import com.google.common.collect.Range;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.example.base.Discount;
import org.example.hotel.HotelId;
import org.example.order.OrderId;

@Data
@Entity
public class Room {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private Long id;

  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "hotel_id")),
    @AttributeOverride(name = "name", column = @Column(name = "hotel_name"))
  })
  @Embedded
  private HotelId hotelId;

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

  public boolean open(RoomCard roomCard) {
    if (roomCard == null) {
      throw new RuntimeException("房卡不能为空");
    }
    if (roomCard.getKey().equals(this.getRoomDoor().getRoomLock().getKey())) {
      this.getRoomDoor().setRoomDoorStatus(RoomDoorStatus.OPEN);
      return true;
    }
    return false;
  }

  public boolean couldBeReserved() {
    return getStatus() == RoomStatus.FREE;
  }

  public double getDiscountPrice(Date checkInTime) {
    return getPrice() * getDiscount(checkInTime);
  }

  private static double getDiscount(Date checkInTime) {
    final int dayOfWeek = getDayOfWeek(checkInTime);
    if (Range.closed(Calendar.MONDAY, Calendar.FRIDAY).contains(dayOfWeek)) {
      return 0.5;
    } else {
      return 1;
    }
  }

  private static int getDayOfWeek(Date checkInTime) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(checkInTime);
    return calendar.get(Calendar.DAY_OF_WEEK);
  }

  public boolean canBeReserved() {
    return status == RoomStatus.FREE;
  }
}
