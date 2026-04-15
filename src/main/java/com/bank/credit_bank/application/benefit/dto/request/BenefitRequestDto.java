package com.bank.credit_bank.application.benefit.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BenefitRequestDto(
        Long id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Integer pointEarned,
        Boolean hasDiscount,
        BigDecimal multiplierPoints,
        Long cardId
) {
}
