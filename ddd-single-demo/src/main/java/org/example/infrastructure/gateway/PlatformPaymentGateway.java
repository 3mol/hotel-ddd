package org.example.infrastructure.gateway;

import org.example.domain.order.PayStatus;
import org.springframework.stereotype.Component;

@Component
public interface PlatformPaymentGateway<REFUNDED_REQ, PAYMENT> {
  PaymentPlatform getPaymentPlatform();

  void receiveRefundedPayment(REFUNDED_REQ payment);

  PayStatus getPaymentStatusFromPlatform(String thirdPartySerialNumber);
}
