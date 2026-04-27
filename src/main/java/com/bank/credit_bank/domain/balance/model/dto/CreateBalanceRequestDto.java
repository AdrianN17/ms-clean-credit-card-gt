package com.bank.credit_bank.domain.balance.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateBalanceRequestDto (
        Long id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        String currency,
        BigDecimal exchangeRate,
        Long cardId,
        BigDecimal total,
        BigDecimal old,
        BigDecimal available,
        LocalDate startDate,
        LocalDate endDate
){}
