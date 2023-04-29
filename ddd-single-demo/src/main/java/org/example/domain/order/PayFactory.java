package org.example.domain.order;

import java.util.Date;
import java.util.UUID;
import org.example.domain.room.Room;

public class PayFactory {

  /** 预约款比例 */
  public static final double DEPOSIT_PROPORTION = 0.20;
  /** 尾款比例 */
  public static final double FINAL_PAYMENT_PROPORTION = 1 - DEPOSIT_PROPORTION;
  /** 押金 */
  public static final int DEPOSIT_CHARGE = 30;

  public static Pay buildRefundPay(Room targetRoom, PayType payType, PayStatus payStatus) {
    final String pid = UUID.randomUUID().toString();
    final Pay pay = new Pay();
    pay.setId(pid);
    pay.setMethod(null);
    pay.setSerialNumber("");
    pay.setThirdPartySerialNumber("");
    pay.setType(payType);
    pay.setStatus(payStatus);
    pay.setCreatedAt(new Date());
    pay.setPaidAt(null);
    if (payType == PayType.DEPOSIT) {
      pay.setAmount(targetRoom.getPrice() * DEPOSIT_PROPORTION);
    } else if (payType == PayType.FINAL_PAYMENT) {
      pay.setAmount(targetRoom.getPrice() * FINAL_PAYMENT_PROPORTION);
    } else if (payType == PayType.DEPOSIT_CHARGE) {
      pay.setAmount(DEPOSIT_CHARGE);
    }
    return pay;
  }

  public static Pay buildRefundPay(Pay pay, double radio) {
    final Pay refundPay = new Pay();
    final String pid = UUID.randomUUID().toString();
    refundPay.setId(pid);
    refundPay.setMethod(pay.getMethod());
    refundPay.setSerialNumber(pay.getSerialNumber());
    refundPay.setThirdPartySerialNumber(pay.getThirdPartySerialNumber());
    refundPay.setType(PayType.DEPOSIT_REFUND);
    refundPay.setStatus(PayStatus.REFUNDED);
    refundPay.setCreatedAt(new Date());
    refundPay.setPaidAt(new Date());
    refundPay.setAmount(pay.getAmount() * radio);
    return refundPay;
  }
}
