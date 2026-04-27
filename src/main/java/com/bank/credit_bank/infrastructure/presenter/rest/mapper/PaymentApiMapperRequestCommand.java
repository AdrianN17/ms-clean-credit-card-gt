package com.bank.credit_bank.infrastructure.presenter.rest.mapper;

import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.PaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.PaymentResponse;

import java.util.UUID;

public interface PaymentApiMapperRequestCommand {
    CardProcessPaymentCommand toCommand(PaymentRequest request, Long cardId);
    CardCancelPaymentCommand toCommandId(UUID paymentId, Long cardId);
    PaymentResponse toResponse(LoadPaymentView view);
}
