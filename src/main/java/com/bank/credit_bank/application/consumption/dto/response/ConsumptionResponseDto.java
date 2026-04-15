package com.bank.credit_bank.application.consumption.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConsumptionResponseDto (
        UUID id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        String currency,
        BigDecimal exchangeRate,
        BigDecimal amount,
        LocalDateTime consumptionDate,
        LocalDateTime consumptionApprobationDate,
        Long cardId,
        String sellerName
){
}
