package com.bank.credit_bank.domain.benefit.model.factory;

import com.bank.credit_bank.domain.benefit.model.entities.Benefit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface BenefitFactory {

    Benefit create(Long benefitId,
                   Long cardId,
                   Integer totalPoints,
                   Boolean hasDiscount,
                   BigDecimal multiplierPoints,
                   Integer status,
                   LocalDateTime createdDate,
                   LocalDateTime updatedDate);

    Benefit create(Long benefitId,
                   Long cardId,
                   Boolean hasDiscount,
                   BigDecimal multiplierPoints);
}

