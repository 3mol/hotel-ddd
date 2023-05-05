package org.example.order;

import java.util.Date;
import lombok.Data;
import org.example.client.user.Customer;
import org.example.client.user.UserId;
import org.example.room.RoomId;

@Data
public class BookingReq {
  private RoomId roomId;
  private Customer customer;
  private String phone;
  private Date checkInDate;
  private UserId userId;
}
