package com.bank.credit_bank.application.payment.business;

import com.bank.credit_bank.domain.payment.model.entities.Payment;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import java.util.UUID;

public interface BusinessServicePayment {
    Payment get(Long cardId, UUID paymentId);

    PaymentId save(Payment payment);
}
