package org.example.application.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.domain.payment.PaymentDomainEvent;
import org.example.domain.payment.PaymentId;

@Data
@AllArgsConstructor
public class PaymentRefundedEvent implements PaymentDomainEvent {
  PaymentId paymentId;
  /** 第三方流水号 */
  private String thirdPartySerialNumber;
}
