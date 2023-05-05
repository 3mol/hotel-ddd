package org.example.hotel;

import lombok.Data;

@Data
public class CheckedInEventHandler {
  private Hotel hotel;

  public CheckedInEventHandler(Hotel hotel) {
    this.hotel = hotel;
  }

  public void handle(CheckedInEvent event) {
    // 1. 根据订单号查询订单
    System.out.println("CheckedInEventHandler.handle = event:" + event);
  }
}
