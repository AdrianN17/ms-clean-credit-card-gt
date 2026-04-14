package com.bank.credit_bank.domain.consumption.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConsumptionCreatedEvent(
        UUID consumptionId,
        Long cardId,
        BigDecimal consumptionAmount,
        Integer currency,
        BigDecimal exchangeRate,
        String sellerName,
        LocalDateTime consumptionDate
) implements DomainEvent {
    @Override
    public String eventType() {
        return "ConsumptionCreated";
    }
}

