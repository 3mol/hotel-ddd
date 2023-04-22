package org.example.domain.hotel;

import lombok.Data;

@Data
public class ReserveRoomEventHandler {
  private Hotel hotel;

  public ReserveRoomEventHandler(Hotel hotel) {
    this.hotel = hotel;
  }

  public void handle(ReserveRoomEvent event) {
    // 1. 根据订单号查询订单
    System.out.println("ReserveRoomEventHandler.handle = event:" + event);
  }
}
