package org.example.domain.hotel;

public class CheckOutEventHandler {
  private final Hotel hotel;

  public CheckOutEventHandler(Hotel hotel) {
    this.hotel = hotel;
  }

  public void handle(CheckOutEvent event) {
    System.out.println("ReserveRoomEventHandler.handle = event:" + event);
  }
}
