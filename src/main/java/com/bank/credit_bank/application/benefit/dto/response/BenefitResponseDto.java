package com.bank.credit_bank.application.benefit.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BenefitResponseDto(
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
