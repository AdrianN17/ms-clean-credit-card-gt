package com.bank.credit_bank.infrastructure.presenter.soap.mapper;

import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ControlPaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.InitiatePaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.PaymentResponse;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.PaymentResponseList;

import java.util.Objects;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.presenter.soap.constants.PaymentMapperCommandMessageConstants.PAYMENT_APPROBATION_DATE_NOT_NULL;
import static com.bank.credit_bank.infrastructure.presenter.soap.constants.PaymentMapperCommandMessageConstants.PAYMENT_DATE_NOT_NULL;
import static com.bank.credit_bank.infrastructure.presenter.soap.util.SoapMapperResponse.toXMLGregorianCalendar;

public class PaymentApiMapperRequestCommandImpl implements PaymentApiMapperRequestCommand {

    @Override
    public CardProcessPaymentCommand toCommand(InitiatePaymentRequest request) {
        return new CardProcessPaymentCommand(
                request.getData().getAmount(),
                request.getData().getCurrency(),
                request.getData().getCategory(),
                request.getCardId(),
                request.getData().getChannel(),
                request.getData().getPointsUsed()
        );
    }

    @Override
    public CardCancelPaymentCommand toCommandId(ControlPaymentRequest paymentId) {
        return new CardCancelPaymentCommand(
                UUID.fromString(paymentId.getPaymentId()),
                paymentId.getCardId()
        );
    }

    @Override
    public PaymentResponseList toResponse(LoadPaymentView view) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setChannel(view.channelPayment());
        paymentResponse.setCurrency(view.currency());
        paymentResponse.setAmount(view.amount());
        paymentResponse.setCategory(view.category());
        paymentResponse.setPaymentDate(toXMLGregorianCalendar(
                Objects.requireNonNull(view.paymentDate(), PAYMENT_DATE_NOT_NULL)));
        paymentResponse.setPaymentApprobationDate(toXMLGregorianCalendar(
                Objects.requireNonNull(view.paymentApprobationDate(), PAYMENT_APPROBATION_DATE_NOT_NULL)));

        PaymentResponseList responseList = new PaymentResponseList();
        responseList.getItem().add(paymentResponse);
        return responseList;
    }
}

