package org.example.base;

import javax.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
  @Resource private ApplicationEventPublisher applicationEventPublisher;

  public void publish(DomainEvent event) {
    applicationEventPublisher.publishEvent(event);
  }
}
