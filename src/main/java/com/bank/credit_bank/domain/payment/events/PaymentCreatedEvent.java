package com.bank.credit_bank.domain.payment.events;

import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentCreatedEvent(
        UUID paymentId,
        Long cardId,
        BigDecimal paymentAmount,
        Integer currency,
        BigDecimal exchangeRate,
        Integer category,
        Integer channelPayment,
        LocalDateTime paymentDate
) implements DomainEvent {
    @Override
    public String eventType() {
        return "PaymentCreated";
    }
}

