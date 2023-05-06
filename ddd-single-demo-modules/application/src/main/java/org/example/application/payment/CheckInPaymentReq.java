package org.example.application.payment;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.order.PayMethod;
import org.example.domain.payment.PaymentId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInPaymentReq {
  private List<PaymentId> paymentIds;
  private PayMethod payMethod;
  private String thirdPartySerialNumber;
}
