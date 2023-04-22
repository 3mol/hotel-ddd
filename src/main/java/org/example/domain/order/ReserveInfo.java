package org.example.domain.order;

import java.util.Date;
import lombok.Data;
import org.example.domain.user.Customer;

@Data
public class ReserveInfo {
  private Customer customer;
  private Date reserveTime;
}
