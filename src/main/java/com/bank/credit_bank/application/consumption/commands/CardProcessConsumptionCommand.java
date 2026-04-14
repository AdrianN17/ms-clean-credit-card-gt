package com.bank.credit_bank.application.consumption.commands;

import com.bank.credit_bank.domain.base.enums.CurrencyEnum;

import java.math.BigDecimal;

public record CardProcessConsumptionCommand(
        BigDecimal amount,
        CurrencyEnum currency,
        Long cardId,
        String sellerName) {
}
