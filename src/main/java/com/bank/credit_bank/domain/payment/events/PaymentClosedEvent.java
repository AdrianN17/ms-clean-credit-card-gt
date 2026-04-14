package com.bank.credit_bank.domain.payment.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.util.UUID;

public record PaymentClosedEvent(
        UUID paymentId
) implements DomainEvent {
    @Override
    public String eventType() {
        return "PaymentClosed";
    }
}

