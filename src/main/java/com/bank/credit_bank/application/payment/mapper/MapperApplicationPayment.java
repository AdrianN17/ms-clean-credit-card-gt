package com.bank.credit_bank.application.payment.mapper;

import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.domain.payment.model.entities.Payment;

public interface MapperApplicationPayment {
    Payment toDomain(PaymentResponseDto dto);

    PaymentRequestDto toDto(Payment payment);
}
