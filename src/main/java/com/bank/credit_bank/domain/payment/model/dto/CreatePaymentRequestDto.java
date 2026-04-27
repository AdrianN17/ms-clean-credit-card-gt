package com.bank.credit_bank.domain.payment.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePaymentRequestDto(
            UUID id,
            Integer status,
            LocalDateTime createdDate,
            LocalDateTime updatedDate,
            String currency,
            BigDecimal exchangeRate,
            BigDecimal amount,
            LocalDateTime paymentDate,
            LocalDateTime paymentApprobationDate,
            String category,
            Long cardId,
            String channelPayment
) {

}
