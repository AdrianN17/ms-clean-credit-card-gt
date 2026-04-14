package com.bank.credit_bank.domain.balance.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

public record BalanceClosedEvent(
        Long balanceId
) implements DomainEvent {
    @Override
    public String eventType() {
        return "BalanceClosed";
    }
}
