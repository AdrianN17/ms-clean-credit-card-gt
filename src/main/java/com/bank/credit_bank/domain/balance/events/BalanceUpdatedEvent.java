package com.bank.credit_bank.domain.balance.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceUpdatedEvent(
        Long balanceId,
        Long cardId,
        BigDecimal available
) implements DomainEvent {
    @Override
    public String eventType() {
        return "BalanceUpdated";
    }
}
