package org.example.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.example.base.DomainEventPublisher;
import org.example.payment.PaymentReceivedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
  @Resource ObjectMapper objectMapper;
  @Resource OrderService orderService;

  public Consumer(ObjectMapper objectMapper, OrderService orderService) {
    this.objectMapper = objectMapper;
    this.orderService = orderService;
  }

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "PaymentReceivedEvent")
  public void consumerOrderBookedEvent(String message) {
    try {
      final PaymentReceivedEvent event =
          objectMapper.readValue(message, PaymentReceivedEvent.class);
      orderService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
