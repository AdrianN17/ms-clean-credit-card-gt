package com.bank.credit_bank.application.card.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoadCardBalanceBenefitView(
        String typeCard,
        String categoryCard,
        BigDecimal creditTotal,
        String currency,
        BigDecimal debtTax,
        String cardStatus,
        Short paymentDate,
        Integer totalPoint,
        Boolean hasDiscount,
        BigDecimal multiplierPoints,
        BigDecimal total,
        BigDecimal old,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal available) {
}
