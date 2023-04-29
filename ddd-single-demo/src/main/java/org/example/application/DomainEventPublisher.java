package org.example.application;

import javax.annotation.Resource;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.payment.PaymentReceivedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
  @Resource private ApplicationEventPublisher applicationEventPublisher;

  public void publish(PaymentReceivedEvent event) {
    applicationEventPublisher.publishEvent(event);
  }

  public void publish(OrderBookedEvent event) {
    applicationEventPublisher.publishEvent(event);
  }
}
