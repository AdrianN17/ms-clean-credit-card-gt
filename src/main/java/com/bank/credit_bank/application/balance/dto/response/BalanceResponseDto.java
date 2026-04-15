package com.bank.credit_bank.application.balance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BalanceResponseDto(
        Long id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Long cardId,
        String currency,
        BigDecimal exchangeRate,
        BigDecimal total,
        BigDecimal old,
        BigDecimal available,
        LocalDate startDate,
        LocalDate endDate
) {
}
