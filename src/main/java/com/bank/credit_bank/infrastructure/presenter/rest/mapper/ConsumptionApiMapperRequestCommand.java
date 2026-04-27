package com.bank.credit_bank.infrastructure.presenter.rest.mapper;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.ConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.ExchangeConsumptionRequestData;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.ConsumptionResponse;

import java.util.UUID;

public interface ConsumptionApiMapperRequestCommand {
    CardProcessConsumptionCommand toCommand(ConsumptionRequest request, Long cardId);
    CardCancelConsumptionCommand toCommandId(UUID consumptionId, Long cardId);
    CardSplitConsumptionCommand toCommandIdR(UUID consumptionId, Long cardId, ExchangeConsumptionRequestData data);
    ConsumptionResponse toResponse(LoadConsumptionView view);
}
