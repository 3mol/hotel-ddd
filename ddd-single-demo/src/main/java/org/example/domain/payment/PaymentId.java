package org.example.domain.payment;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentId {
  private Long id;
  // 流水号，一般与订单号相同
  private String serialNumber;
}
