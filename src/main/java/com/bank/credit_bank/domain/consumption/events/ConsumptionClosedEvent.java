package com.bank.credit_bank.domain.consumption.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.util.UUID;

public record ConsumptionClosedEvent(
        UUID consumptionId
) implements DomainEvent {
    @Override
    public String eventType() {
        return "ConsumptionClosed";
    }
}

