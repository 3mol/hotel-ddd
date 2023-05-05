package org.example.order;

import java.util.List;
import lombok.Data;
import org.example.client.user.Customer;

@Data
public class CheckInReq {
  private OrderId orderId;
  private List<Customer> customer;
  private String phone;
}
