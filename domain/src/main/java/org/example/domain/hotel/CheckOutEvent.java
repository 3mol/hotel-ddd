package org.example.domain.hotel;

import lombok.Data;

@Data
public class CheckOutEvent {
  private String orderNumber;
  private String roomNumber;

  public CheckOutEvent(String orderNumber, String roomNumber) {
    this.orderNumber = orderNumber;
    this.roomNumber = roomNumber;
  }
}
