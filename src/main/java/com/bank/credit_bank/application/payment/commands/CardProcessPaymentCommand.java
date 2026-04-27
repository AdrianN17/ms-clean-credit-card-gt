package com.bank.credit_bank.application.payment.commands;

import java.math.BigDecimal;

public record CardProcessPaymentCommand(
        BigDecimal amount,
        String currency,
        String category,
        Long cardId,
        String channelPayment,
        Integer pointsUsed
) {
}
