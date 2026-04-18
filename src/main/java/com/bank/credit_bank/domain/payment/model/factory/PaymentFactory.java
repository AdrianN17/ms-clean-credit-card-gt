package com.bank.credit_bank.domain.payment.model.factory;

import com.bank.credit_bank.domain.payment.model.entities.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PaymentFactory {

    Payment create(
            UUID id,
            Integer status,
            LocalDateTime createdDate,
            LocalDateTime updatedDate,
            Integer currency,
            BigDecimal exchangeRate,
            BigDecimal amount,
            LocalDateTime paymentDate,
            LocalDateTime paymentApprobationDate,
            Integer category,
            Long cardId,
            Integer channelPayment

    );

    Payment create(
            Integer currency,
            BigDecimal exchangeRate,
            BigDecimal amount,
            Integer category,
            Long cardId,
            Integer channelPayment
    );
}
