package com.bank.credit_bank.domain.payment.model.dto;

import java.math.BigDecimal;

public record CreatePaymentRequestFirstDto(
        String currency,
        BigDecimal exchangeRate,
        BigDecimal amount,
        String category,
        Long cardId,
        String channelPayment
) {

}
