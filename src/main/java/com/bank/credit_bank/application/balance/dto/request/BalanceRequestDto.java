package com.bank.credit_bank.application.balance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BalanceRequestDto(
        Long id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Long cardId,
        Integer currency,
        BigDecimal exchangeRate,
        BigDecimal total,
        BigDecimal old,
        BigDecimal available,
        LocalDate startDate,
        LocalDate endDate
) {
}
