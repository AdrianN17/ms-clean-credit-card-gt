package com.bank.credit_bank.application.payment.port.in;

import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

@FunctionalInterface
public interface CardProcessPaymentUseCase {
    PaymentId processPayment(CardProcessPaymentCommand cardProcessPaymentCommand);
}
