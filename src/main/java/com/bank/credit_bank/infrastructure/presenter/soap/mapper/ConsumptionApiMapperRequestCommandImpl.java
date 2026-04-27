package com.bank.credit_bank.infrastructure.presenter.soap.mapper;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ControlConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ExchangeConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.InitiateConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.ConsumptionResponse;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.ConsumptionResponseList;

import java.util.Objects;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.presenter.soap.constants.ConsumptionMapperCommandMessageConstants.CONSUMPTION_APPROBATION_DATE_NOT_NULL;
import static com.bank.credit_bank.infrastructure.presenter.soap.constants.ConsumptionMapperCommandMessageConstants.CONSUMPTION_DATE_NOT_NULL;
import static com.bank.credit_bank.infrastructure.presenter.soap.util.SoapMapperResponse.toXMLGregorianCalendar;

public class ConsumptionApiMapperRequestCommandImpl implements ConsumptionApiMapperRequestCommand {

    @Override
    public CardProcessConsumptionCommand toCommand(InitiateConsumptionRequest request) {
        return new CardProcessConsumptionCommand(
                request.getData().getAmount(),
                request.getData().getCurrency(),
                request.getCardId(),
                request.getData().getSellerName()
        );
    }

    @Override
    public CardCancelConsumptionCommand toCommandId(ControlConsumptionRequest parameters) {
        return new CardCancelConsumptionCommand(
                UUID.fromString(parameters.getConsumptionId()),
                parameters.getCardId()
        );
    }

    @Override
    public CardSplitConsumptionCommand toCommandIdR(ExchangeConsumptionRequest parameters) {
        return new CardSplitConsumptionCommand(
                UUID.fromString(parameters.getConsumptionId()),
                parameters.getData().getInstallments(),
                parameters.getCardId()
        );
    }

    @Override
    public ConsumptionResponseList toResponse(LoadConsumptionView view) {
        ConsumptionResponse consumptionResponse = new ConsumptionResponse();
        consumptionResponse.setSellerName(view.sellerName());
        consumptionResponse.setCurrency(view.currency());
        consumptionResponse.setAmount(view.amount());
        consumptionResponse.setConsumptionDate(toXMLGregorianCalendar(
                Objects.requireNonNull(view.consumptionDate(), CONSUMPTION_DATE_NOT_NULL)));
        consumptionResponse.setConsumptionApprobationDate(toXMLGregorianCalendar(
                Objects.requireNonNull(view.consumptionApprobationDate(), CONSUMPTION_APPROBATION_DATE_NOT_NULL)));

        ConsumptionResponseList responseList = new ConsumptionResponseList();
        responseList.getItem().add(consumptionResponse);
        return responseList;
    }
}

