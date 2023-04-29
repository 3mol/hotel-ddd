package org.example.domain.payment;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.example.domain.order.PayMethod;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;

@Data
@Entity
public class Payment {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private Long id;

  private PayMethod method; // 支付方式 [支付宝、微信、现金]
  private String serialNumber; // 流水号 [一般与订单号系统]
  private String thirdPartySerialNumber; // 第三方流水号 [支付成功后返回]
  private PayType type; // 类型 [常规、订金、住宿尾款、住宿费用、押金]
  private PayStatus status; // 支付状态 [已支付、未支付、已退款]
  private Double amount; // 支付金额
  private Date createdAt; // 创建时间
  private Date paidAt; // 支付时间

  public PaymentReceivedEvent receivePay(PayMethod payMethod, String thirdPartySerialNumber) {
    this.method = payMethod;
    this.thirdPartySerialNumber = thirdPartySerialNumber;
    this.type = PayType.DEPOSIT;
    this.status = PayStatus.PAID;
    this.paidAt = new Date();
    return new PaymentReceivedEvent(this.id, this.serialNumber, this.type);
  }
}
