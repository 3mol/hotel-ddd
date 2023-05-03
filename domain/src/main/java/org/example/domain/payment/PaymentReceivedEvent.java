package org.example.domain.payment;

import lombok.Data;
import org.example.domain.order.PayType;

@Data
public class PaymentReceivedEvent implements PaymentDomainEvent {
  private final Long id;
  private final String serialNumber;
  private final PayType type;

  public PaymentReceivedEvent(Long id, String serialNumber, PayType type) {
    this.id = id;
    this.serialNumber = serialNumber;
    this.type = type;
  }
}
