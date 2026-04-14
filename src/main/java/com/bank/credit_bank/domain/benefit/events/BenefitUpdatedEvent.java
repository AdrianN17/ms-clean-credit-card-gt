package com.bank.credit_bank.domain.benefit.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;

public record BenefitUpdatedEvent(
        Long benefitId,
        Long cardId,
        Integer totalPoints,
        Boolean hasDiscount,
        BigDecimal multiplierPoints
) implements DomainEvent {
    @Override
    public String eventType() {
        return "BenefitUpdated";
    }
}

