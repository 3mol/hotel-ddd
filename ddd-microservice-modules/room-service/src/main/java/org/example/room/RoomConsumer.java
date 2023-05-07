package org.example.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.example.base.DomainEventPublisher;
import org.example.order.OrderCancelledEvent;
import org.example.order.OrderCheckedInEvent;
import org.example.order.OrderCheckedOutEvent;
import org.example.payment.PaymentReceivedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RoomConsumer {
  @Resource ObjectMapper objectMapper;
  @Resource RoomService roomService;

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "OrderCheckedInEvent")
  public void consumerOrderCheckedInEvent(String message) {
    try {
      final OrderCheckedInEvent event = objectMapper.readValue(message, OrderCheckedInEvent.class);
      roomService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "OrderCancelledEvent")
  public void consumerOrderCancelledEvent(String message) {
    try {
      final OrderCancelledEvent event = objectMapper.readValue(message, OrderCancelledEvent.class);
      roomService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "OrderCheckedOutEvent")
  public void consumerOrderCheckedOutEvent(String message) {
    try {
      final OrderCheckedOutEvent event =
          objectMapper.readValue(message, OrderCheckedOutEvent.class);
      roomService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @KafkaListener(topics = DomainEventPublisher.PREFIX + "PaymentReceivedEvent")
  public void consumerPaymentReceivedEvent(String message) {
    try {
      final PaymentReceivedEvent event =
          objectMapper.readValue(message, PaymentReceivedEvent.class);
      roomService.listen(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
