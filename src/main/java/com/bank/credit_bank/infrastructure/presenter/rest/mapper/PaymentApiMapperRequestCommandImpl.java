package com.bank.credit_bank.infrastructure.presenter.rest.mapper;

import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.PaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.PaymentResponse;

import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.presenter.soap.constants.PaymentMapperCommandMessageConstants.PAYMENT_APPROBATION_DATE_NOT_NULL;
import static com.bank.credit_bank.infrastructure.presenter.soap.constants.PaymentMapperCommandMessageConstants.PAYMENT_DATE_NOT_NULL;

public class PaymentApiMapperRequestCommandImpl implements PaymentApiMapperRequestCommand {

    @Override
    public CardProcessPaymentCommand toCommand(PaymentRequest request, Long cardId) {
        return new CardProcessPaymentCommand(
                request.getAmount(),
                request.getCurrency(),
                request.getCategory(),
                cardId,
                request.getChannel(),
                request.getPointsUsed()
        );
    }

    @Override
    public CardCancelPaymentCommand toCommandId(UUID paymentId, Long cardId) {
        return new CardCancelPaymentCommand(paymentId, cardId);
    }

    @Override
    public PaymentResponse toResponse(LoadPaymentView view) {
        return new PaymentResponse(
                view.channelPayment(),
                view.currency(),
                view.amount(),
                view.category(),
                Objects.requireNonNull(view.paymentDate(), PAYMENT_DATE_NOT_NULL).atOffset(ZoneOffset.UTC),
                Objects.requireNonNull(view.paymentApprobationDate(), PAYMENT_APPROBATION_DATE_NOT_NULL).atOffset(ZoneOffset.UTC)
        );
    }
}
