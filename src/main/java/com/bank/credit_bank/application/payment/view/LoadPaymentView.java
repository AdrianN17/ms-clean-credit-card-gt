package com.bank.credit_bank.application.payment.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoadPaymentView(
        UUID paymentId,
        BigDecimal amount,
        String currency,
        LocalDateTime paymentDate,
        LocalDateTime paymentApprobationDate,
        String category,
        String cardId,
        String channelPayment
) {
}
