package org.example.application;

public abstract class BaseService {
  protected DomainEventPublisher domainEventPublisher;

  public BaseService(DomainEventPublisher domainEventPublisher) {
    this.domainEventPublisher = domainEventPublisher;
  }
}
