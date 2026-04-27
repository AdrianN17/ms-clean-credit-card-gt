package com.bank.credit_bank.domain.balance.model.dto;

import java.math.BigDecimal;

public record CreateBalanceRequestFirstDto (
        Long id,
        String currency,
        BigDecimal exchangeRate,
        Long cardId,
        BigDecimal total,
        Short paymentDay
){
}
