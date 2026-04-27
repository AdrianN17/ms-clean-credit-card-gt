package com.bank.credit_bank.infrastructure.presenter.soap.mapper;

import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ControlPaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.InitiatePaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.PaymentResponseList;

public interface PaymentApiMapperRequestCommand {
    CardProcessPaymentCommand toCommand(InitiatePaymentRequest request);

    CardCancelPaymentCommand toCommandId(ControlPaymentRequest paymentId);

    PaymentResponseList toResponse(LoadPaymentView view);
}
