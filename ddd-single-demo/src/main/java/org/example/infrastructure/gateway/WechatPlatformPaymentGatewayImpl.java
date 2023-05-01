package org.example.infrastructure.gateway;

import javax.annotation.Resource;
import org.example.application.DomainEventPublisher;
import org.example.application.payment.PaymentRefundedEvent;
import org.example.domain.order.PayStatus;
import org.springframework.stereotype.Component;

@Component
public class WechatPlatformPaymentGatewayImpl
    implements PlatformPaymentGateway<WechatRefundReq, WechatPayment> {
  @Resource DomainEventPublisher domainEventPublisher;

  @Override
  public PaymentPlatform getPaymentPlatform() {
    return PaymentPlatform.WECHAT;
  }

  @Override
  public void receiveRefundedPayment(WechatRefundReq payment) {
    // 模拟从微信平台接收退款信息
    if (payment.getRefundStatus() != WechatRefundReq.RefundStatus.SUCCESS) {
      throw new RuntimeException("退款失败，payment:" + payment);
    }
    final PaymentRefundedEvent paymentRefundedEvent =
        new PaymentRefundedEvent(payment.getSerialNumber(), payment.getRefundAmount());
    domainEventPublisher.publish(paymentRefundedEvent);
  }

  @Override
  public PayStatus getPaymentStatusFromPlatform(String thirdPartySerialNumber) {
    return PayStatus.PAID;
  }

  @Override
  public void requestRefundPayment(String thirdPartySerialNumber, Double amount) {
    // pass
  }
}
