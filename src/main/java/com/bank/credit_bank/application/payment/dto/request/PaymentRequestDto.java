package com.bank.credit_bank.application.payment.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentRequestDto(
        UUID id,
        Integer status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Integer currency,
        BigDecimal exchangeRate,
        BigDecimal amount,
        LocalDateTime paymentDate,
        LocalDateTime paymentApprobationDate,
        Integer category,
        Long cardId,
        Integer channelPayment
) {

}
