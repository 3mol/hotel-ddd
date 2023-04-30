package org.example.application.order;

import java.util.List;
import lombok.Data;
import org.example.domain.order.OrderId;
import org.example.domain.user.Customer;

@Data
public class CheckInReq {
  private OrderId orderId;
  private List<Customer> customer;
  private String phone;
}
