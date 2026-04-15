package com.bank.credit_bank.application.payment.port.out;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface PaymentFindByIdPort {
    Optional<PaymentResponseDto> load(UUID paymentId, String cardId, CurrencyRequestDto currency);
}
