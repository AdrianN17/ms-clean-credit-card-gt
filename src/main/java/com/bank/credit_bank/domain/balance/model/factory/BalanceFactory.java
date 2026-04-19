package com.bank.credit_bank.domain.balance.model.factory;

import com.bank.credit_bank.domain.balance.model.entities.Balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BalanceFactory {

    Balance create(
            Long id,
            Integer currency,
            BigDecimal exchangeRate,
            Long cardId,
            BigDecimal total,
            Short paymentDay
    );

    Balance create(
            Long id,
            Integer status,
            LocalDateTime createdDate,
            LocalDateTime updatedDate,
            Integer currency,
            BigDecimal exchangeRate,
            Long cardId,
            BigDecimal total,
            BigDecimal old,
            BigDecimal available,
            LocalDate startDate,
            LocalDate endDate
    );
}
