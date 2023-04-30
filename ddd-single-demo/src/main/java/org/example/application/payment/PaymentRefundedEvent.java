package org.example.application.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.domain.payment.PaymentDomainEvent;

@Data
@AllArgsConstructor
public class PaymentRefundedEvent implements PaymentDomainEvent {
  /** 第三方流水号 */
  private String thirdPartySerialNumber;

  private Double refundedAmount;
}
