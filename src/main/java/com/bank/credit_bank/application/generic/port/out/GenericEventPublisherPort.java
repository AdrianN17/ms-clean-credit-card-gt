package com.bank.credit_bank.application.generic.port.out;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

@FunctionalInterface
public interface GenericEventPublisherPort {
    void publish(DomainEvent event);
}
