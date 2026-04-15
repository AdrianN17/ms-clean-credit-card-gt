package com.bank.credit_bank.application.payment.port.in;

import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

@FunctionalInterface
public interface PaymentCancelUseCase {
    PaymentId execute(CardCancelPaymentCommand cardCancelPaymentCommand);
}
