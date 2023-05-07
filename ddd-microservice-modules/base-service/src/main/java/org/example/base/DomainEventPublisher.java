package org.example.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
  public static final String PREFIX = "x-event-";
  @Resource private ApplicationEventPublisher applicationEventPublisher;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;
  @Resource private ObjectMapper objectMapper;

  public void publish(DomainEvent event) {
    applicationEventPublisher.publishEvent(event);
    try {
      final String json = objectMapper.writeValueAsString(event);
      sendMessage(PREFIX + event.getClass().getSimpleName(), json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(String topic, String message) {
    kafkaTemplate.send(topic, message);
  }
}
