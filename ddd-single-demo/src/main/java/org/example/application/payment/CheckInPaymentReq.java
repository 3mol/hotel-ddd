package org.example.application.payment;

import java.util.List;
import lombok.Data;
import org.example.domain.order.PayMethod;
import org.example.domain.payment.PaymentId;

@Data
public class CheckInPaymentReq {
  private List<PaymentId> paymentIds;
  private PayMethod payMethod;
  private String thirdPartySerialNumber;
}
