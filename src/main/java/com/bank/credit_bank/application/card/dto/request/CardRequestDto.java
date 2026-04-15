package com.bank.credit_bank.application.card.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CardRequestDto(
        Long id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Integer typeCard,
        Integer categoryCard,
        Integer currency,
        BigDecimal exchangeRate,
        BigDecimal creditTotal,
        BigDecimal debtTax,
        Integer cardStatus,
        Long cardAccountId,
        Short paymentDay
) {
}
