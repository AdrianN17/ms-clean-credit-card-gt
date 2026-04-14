package com.bank.credit_bank.application.consumption.commands;

import java.math.BigDecimal;

public record CardProcessConsumptionCommand(
        BigDecimal amount,
        Integer currency,
        Long cardId,
        String sellerName) {
}
