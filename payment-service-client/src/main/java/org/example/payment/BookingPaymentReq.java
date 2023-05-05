package org.example.payment;

import lombok.Data;

@Data
public class BookingPaymentReq {
  private PaymentId paymentId;
  private PayMethod payMethod;
  private String thirdPartySerialNumber;
}
