package org.example.payment;

import lombok.Data;

@Data
public class PayEvent {
  private String number;
  private PayType payType;
  private PayMethod payMethod;

  public PayEvent(String number, PayType payType, PayMethod payMethod) {
    this.number = number;
    this.payType = payType;
    this.payMethod = payMethod;
  }
}
