package org.example.application;

import java.util.UUID;
import org.example.domain.order.Order;
import org.example.domain.order.Pay;
import org.example.domain.order.PayEvent;
import org.example.domain.order.PayMethod;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;
import org.example.event.PayEventHandler;

public class PayService {
  public static void pay(Order order, PayType payType, PayMethod payMethod, double amount) {
    final Pay payTarget =
        order.getPays().stream().filter(pay -> pay.getType() == payType).findFirst().orElseThrow();
    if (amount != payTarget.getAmount()) {
      throw new RuntimeException("支付金额不正确");
    }
    final String s = UUID.randomUUID().toString();
    payTarget.setThirdPartySerialNumber(s);
    payTarget.setMethod(payMethod);
    payTarget.setStatus(PayStatus.PAID);
    new PayEventHandler().handle(new PayEvent(order.getNumber(), payType, payMethod));
  }
}
