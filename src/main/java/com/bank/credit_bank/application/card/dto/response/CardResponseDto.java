package com.bank.credit_bank.application.card.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CardResponseDto(
        Long id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        String typeCard,
        String categoryCard,
        String currency,
        BigDecimal exchangeRate,
        BigDecimal creditTotal,
        BigDecimal debtTax,
        String cardStatus,
        Long cardAccountId,
        Short paymentDay
) {
}
