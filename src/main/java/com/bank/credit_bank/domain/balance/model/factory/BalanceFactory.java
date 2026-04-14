package com.bank.credit_bank.domain.balance.model.factory;

import com.bank.credit_bank.domain.balance.model.entities.Balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BalanceFactory {

    Balance create(Long balanceId,
                   Long cardId,
                   BigDecimal total,
                   LocalDate startDate,
                   LocalDate endDate,
                   Integer currency,
                   BigDecimal exchangeRate,
                   Integer status,
                   LocalDateTime createdDate,
                   LocalDateTime updatedDate);

    Balance create(Long balanceId,
                   Long cardId,
                   BigDecimal total,
                   BigDecimal old,
                   BigDecimal available,
                   LocalDate startDate,
                   LocalDate endDate,
                   Integer currency,
                   BigDecimal exchangeRate);
}
