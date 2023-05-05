package org.example.payment;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatPayment {
  private String serialNumber;
  private Double amount;
  private String status;
  private Date paidAt;
}
