package com.bank.credit_bank.domain.card.model.factory;

import com.bank.credit_bank.domain.card.model.entities.Card;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CardFactory {

    Card create(Long cardId,
                Integer typeCard,
                Integer categoryCard,
                Long cardAccountId,
                Short paymentDay,
                BigDecimal creditTotal,
                BigDecimal debtTax,
                Integer currency,
                BigDecimal exchangeRate,
                Integer cardStatus,
                Integer status,
                LocalDateTime createdDate,
                LocalDateTime updatedDate);

    Card create(Long cardId,
                Integer typeCard,
                Integer categoryCard,
                Long cardAccountId,
                Short paymentDay,
                BigDecimal creditTotal,
                BigDecimal debtTax,
                Integer currency,
                BigDecimal exchangeRate);
}