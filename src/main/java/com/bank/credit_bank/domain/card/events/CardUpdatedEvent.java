package com.bank.credit_bank.domain.card.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

public record CardUpdatedEvent(
        Long cardId,
        Integer cardStatus
) implements DomainEvent {
    @Override
    public String eventType() {
        return "CardUpdated";
    }
}
