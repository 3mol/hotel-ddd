package org.example.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.example.base.DomainEventPublisher;
import org.example.order.OrderBookedEvent;
import org.example.order.OrderCancelledEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {
  @Resource ObjectMapper objectMapper;
  @Resource PaymentService paymentService;

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "OrderBookedEvent")
  public void consumerOrderBookedEvent(String message) {
    try {
      final OrderBookedEvent event = objectMapper.readValue(message, OrderBookedEvent.class);
      paymentService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "OrderCancelledEvent")
  public void consumerOrderCancelledEvent(String message) {
    try {
      final OrderCancelledEvent event = objectMapper.readValue(message, OrderCancelledEvent.class);
      paymentService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "PaymentRefundedEvent")
  public void consumerPaymentRefundedEvent(String message) {
    try {
      final PaymentRefundedEvent event =
          objectMapper.readValue(message, PaymentRefundedEvent.class);
      paymentService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
