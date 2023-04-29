package org.example.application.payment;

import lombok.Data;
import org.example.domain.order.PayMethod;
import org.example.domain.payment.PaymentId;

@Data
public class BookingPaymentReq {
  private PaymentId paymentId;
  private PayMethod payMethod;
  private String thirdPartySerialNumber;
}
