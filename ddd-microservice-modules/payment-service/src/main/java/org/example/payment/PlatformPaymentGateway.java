package org.example.payment;

import org.springframework.stereotype.Component;

@Component
public interface PlatformPaymentGateway<REFUNDED_REQ, PAYMENT> {
  PaymentPlatform getPaymentPlatform();

  void receiveRefundedPayment(REFUNDED_REQ payment);

  String getPaymentStatusFromPlatform(String thirdPartySerialNumber);

  void requestRefundPayment(String thirdPartySerialNumber, Double amount);
}
