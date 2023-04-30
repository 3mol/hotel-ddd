package org.example.infrastructure.gateway;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.order.PayStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatPayment {
  private String serialNumber;
  private Double amount;
  private PayStatus status;
  private Date paidAt;
}
