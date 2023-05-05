package org.example.payment;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInPaymentReq {
  private List<PaymentId> paymentIds;
  private PayMethod payMethod;
  private String thirdPartySerialNumber;
}
