package com.bank.credit_bank.infrastructure.presenter.rest.mapper;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.ConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.ExchangeConsumptionRequestData;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.ConsumptionResponse;

import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.presenter.soap.constants.ConsumptionMapperCommandMessageConstants.CONSUMPTION_APPROBATION_DATE_NOT_NULL;
import static com.bank.credit_bank.infrastructure.presenter.soap.constants.ConsumptionMapperCommandMessageConstants.CONSUMPTION_DATE_NOT_NULL;

public class ConsumptionApiMapperRequestCommandImpl implements ConsumptionApiMapperRequestCommand {

    @Override
    public CardProcessConsumptionCommand toCommand(ConsumptionRequest request, Long cardId) {
        return new CardProcessConsumptionCommand(
                request.getAmount(),
                request.getCurrency(),
                cardId,
                request.getSellerName()
        );
    }

    @Override
    public CardCancelConsumptionCommand toCommandId(UUID consumptionId, Long cardId) {
        return new CardCancelConsumptionCommand(consumptionId, cardId);
    }

    @Override
    public CardSplitConsumptionCommand toCommandIdR(UUID consumptionId, Long cardId, ExchangeConsumptionRequestData data) {
        return new CardSplitConsumptionCommand(consumptionId, data.getInstallments(), cardId);
    }

    @Override
    public ConsumptionResponse toResponse(LoadConsumptionView view) {
        return new ConsumptionResponse(
                view.sellerName(),
                view.currency(),
                view.amount(),
                Objects.requireNonNull(view.consumptionDate(), CONSUMPTION_DATE_NOT_NULL).atOffset(ZoneOffset.UTC),
                Objects.requireNonNull(view.consumptionApprobationDate(), CONSUMPTION_APPROBATION_DATE_NOT_NULL).atOffset(ZoneOffset.UTC)
        );
    }
}
