package org.example.application;

import javax.annotation.Resource;
import org.example.application.payment.PaymentRefundedEvent;
import org.example.domain.order.OrderBookedEvent;
import org.example.domain.order.OrderCancelledEvent;
import org.example.domain.order.OrderCheckedInEvent;
import org.example.domain.order.OrderCheckedOutEvent;
import org.example.domain.payment.PaymentReceivedEvent;
import org.example.domain.room.RoomAppendedEvent;
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

  public void publish(RoomAppendedEvent event) {
    applicationEventPublisher.publishEvent(event);
  }

  public void publish(OrderCheckedInEvent orderCheckedInEvent) {
    applicationEventPublisher.publishEvent(orderCheckedInEvent);
  }

  public void publish(OrderCheckedOutEvent orderCheckedOutEvent) {
    applicationEventPublisher.publishEvent(orderCheckedOutEvent);
  }

  public void publish(OrderCancelledEvent orderCancelledEvent) {
    applicationEventPublisher.publishEvent(orderCancelledEvent);
  }

  public void publish(PaymentRefundedEvent paymentRefundedEvent) {
    applicationEventPublisher.publishEvent(paymentRefundedEvent);
  }
}
