package com.bank.credit_bank.application.payment.commands;

import java.math.BigDecimal;

public record CardProcessPaymentCommand(
        BigDecimal amount,
        Integer currency,
        Integer category,
        Long cardId,
        Integer channelPayment,
        Integer pointsUsed
) {
}
