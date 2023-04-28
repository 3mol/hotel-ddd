package org.example.domain.order;

import java.util.Date;
import lombok.Data;

@Data
public class Pay {
  private String id;
  private PayMethod method; // 支付方式 [支付宝、微信、现金]
  private String serialNumber; // 流水号 [一般与订单号系统]
  private String thirdPartySerialNumber; // 第三方流水号 [支付成功后返回]
  private PayType type; // 类型 [常规、订金、住宿尾款、住宿费用、押金]
  private PayStatus status; // 支付状态 [已支付、未支付、已退款]
  private double amount; // 支付金额
  private Date createdAt; // 创建时间
  private Date paidAt; // 支付时间
}
