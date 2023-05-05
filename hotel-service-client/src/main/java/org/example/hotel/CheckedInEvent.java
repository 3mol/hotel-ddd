package org.example.hotel;

import lombok.Data;

@Data
public class CheckedInEvent {
  private String orderNumber;

  public CheckedInEvent(String orderNumber) {
    this.orderNumber = orderNumber;
  }
}
