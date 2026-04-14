package com.bank.credit_bank.domain.benefit.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

public record BenefitClosedEvent(
        Long benefitId
) implements DomainEvent {
    @Override
    public String eventType() {
        return "BenefitClosed";
    }
}

