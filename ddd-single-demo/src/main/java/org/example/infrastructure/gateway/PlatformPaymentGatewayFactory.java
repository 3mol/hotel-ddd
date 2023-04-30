package org.example.infrastructure.gateway;

import java.util.List;
import javax.annotation.Resource;
import org.example.domain.order.PayMethod;
import org.springframework.stereotype.Component;

@Component
public class PlatformPaymentGatewayFactory {
  @Resource List<PlatformPaymentGateway<?, ?>> platformPaymentGateways;

  public PlatformPaymentGateway<?, ?> create(PayMethod paymentPlatform) {
    switch (paymentPlatform) {
      case WECHAT:
        return platformPaymentGateways.stream()
            .filter(i -> i.getPaymentPlatform() == PaymentPlatform.WECHAT)
            .findFirst()
            .orElseThrow();
      case ALIPAY:
        return platformPaymentGateways.stream()
            .filter(i -> i.getPaymentPlatform() == PaymentPlatform.ALIPAY)
            .findFirst()
            .orElseThrow();
      default:
        throw new IllegalArgumentException("Unsupported payment platform: " + paymentPlatform);
    }
  }
}
