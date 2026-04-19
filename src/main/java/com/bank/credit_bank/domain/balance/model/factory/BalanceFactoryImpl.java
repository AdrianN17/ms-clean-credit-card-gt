package com.bank.credit_bank.domain.balance.model.factory;

import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.entities.BalanceConsumo;
import com.bank.credit_bank.domain.balance.model.entities.BalancePago;
import com.bank.credit_bank.domain.balance.model.enums.BalanceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BalanceFactoryImpl implements BalanceFactory {

    @Override
    public Balance create(Long id,
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
                          LocalDate endDate,
                          BalanceType balanceType) {

        return switch (balanceType) {
            case PAYMENT -> BalancePago.builder()
                    .balanceId(id)
                    .status(status)
                    .createdDate(createdDate)
                    .updatedDate(updatedDate)
                    .currency(currency, exchangeRate)
                    .cardId(cardId)
                    .total(total)
                    .old(old)
                    .available(available)
                    .dateRange(startDate, endDate)
                    .build();
            case CONSUMPTION -> BalanceConsumo.builder()
                    .balanceId(id)
                    .status(status)
                    .createdDate(createdDate)
                    .updatedDate(updatedDate)
                    .currency(currency, exchangeRate)
                    .cardId(cardId)
                    .total(total)
                    .old(old)
                    .available(available)
                    .dateRange(startDate, endDate)
                    .build();
        };
    }

    @Override
    public Balance create(Long id,
                          Integer currency,
                          BigDecimal exchangeRate,
                          Long cardId,
                          BigDecimal total,
                          Short paymentDay,
                          BalanceType balanceType) {
        return switch (balanceType) {
            case PAYMENT -> BalancePago.builder()
                    .balanceId(id)
                    .currency(currency, exchangeRate)
                    .cardId(cardId)
                    .total(total)
                    .dateRange(paymentDay)
                    .build();
            case CONSUMPTION -> BalanceConsumo.builder()
                    .balanceId(id)
                    .currency(currency, exchangeRate)
                    .cardId(cardId)
                    .total(total)
                    .dateRange(paymentDay)
                    .build();
        };
    }

}
