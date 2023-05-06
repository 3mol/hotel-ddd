package org.example.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRefundedEvent implements PaymentDomainEvent {
  /** 第三方流水号 */
  private String thirdPartySerialNumber;

  private Double refundedAmount;
}
