package org.example.order;

import java.util.Date;
import lombok.Data;
import org.example.client.user.Customer;

@Data
public class ReserveInfo {
  private Customer customer;
  private Date reserveDate;
}
