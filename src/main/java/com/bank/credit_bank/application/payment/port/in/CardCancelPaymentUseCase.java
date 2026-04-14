package com.bank.credit_bank.application.payment.port.in;

import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;

import java.util.UUID;

@FunctionalInterface
public interface CardCancelPaymentUseCase {
    UUID cancelPayment(CardCancelPaymentCommand cardCancelPaymentCommand);
}
