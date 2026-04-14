package com.bank.credit_bank.domain.card.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;

public record CardCreatedEvent(
        Long cardId,
        Integer typeCard,
        Integer categoryCard,
        Long cardAccountId,
        Short paymentDay,
        BigDecimal creditTotal,
        BigDecimal debtTax,
        Integer currency,
        BigDecimal exchangeRate,
        Integer cardStatus
) implements DomainEvent {
    @Override
    public String eventType() {
        return "CardCreated";
    }
}
