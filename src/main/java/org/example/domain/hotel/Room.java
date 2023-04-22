package org.example.domain.hotel;

import org.example.domain.order.Discount;

public class Room {
  private String id;
  private String type; // 类型 [单人间、双人间]
  private String status; // 状态 [空闲、已预定、已入住]
  private double price; // 价格
  private String number; // 房间号
  private String floor; // 楼层
  private Discount discount; // 折扣
  // private Reserve reserve; // 预定
  // private CheckIn checkIn; // 登记入住

  public Room() {}

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getStatus() {
    return status;
  }

  public double getPrice() {
    return price;
  }

  public String getNumber() {
    return number;
  }

  public String getFloor() {
    return floor;
  }

  public Discount getDiscount() {
    return discount;
  }
}
