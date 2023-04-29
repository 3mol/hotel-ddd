package org.example.application.order;

import java.util.Date;
import lombok.Data;
import org.example.domain.order.RoomId;
import org.example.domain.user.Customer;
import org.example.domain.user.UserId;

@Data
public class BookingReq {
  private RoomId roomId;
  private Customer customer;
  private String phone;
  private Date checkInDate;
  private UserId userId;
}
