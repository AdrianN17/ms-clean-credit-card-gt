package com.bank.credit_bank.domain.balance.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceCreatedEvent(
        Long balanceId,
        Long cardId,
        BigDecimal total,
        BigDecimal old,
        BigDecimal available,
        LocalDate startDate,
        LocalDate endDate
) implements DomainEvent {
    @Override
    public String eventType() {
        return "BalanceCreated";
    }
}
