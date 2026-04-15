package com.bank.credit_bank.application.consumption.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoadConsumptionView(
        UUID consumptionId,
        BigDecimal amount,
        String currency,
        LocalDateTime consumptionDate,
        LocalDateTime consumptionApprobationDate,
        String cardId,
        String sellerName) {
}
