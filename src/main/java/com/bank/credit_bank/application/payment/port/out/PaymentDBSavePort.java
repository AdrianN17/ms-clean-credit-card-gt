package com.bank.credit_bank.application.payment.port.out;

import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import java.util.Optional;

public interface PaymentDBSavePort {
    Optional<PaymentId> save(PaymentRequestDto payment);
}
