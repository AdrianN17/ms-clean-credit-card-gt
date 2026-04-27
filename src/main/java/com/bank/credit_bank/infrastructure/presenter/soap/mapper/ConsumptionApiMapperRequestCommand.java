package com.bank.credit_bank.infrastructure.presenter.soap.mapper;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ControlConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ExchangeConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.InitiateConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.ConsumptionResponseList;

public interface ConsumptionApiMapperRequestCommand {
    CardProcessConsumptionCommand toCommand(InitiateConsumptionRequest request);

    CardCancelConsumptionCommand toCommandId(ControlConsumptionRequest parameters);

    CardSplitConsumptionCommand toCommandIdR(ExchangeConsumptionRequest parameters);

    ConsumptionResponseList toResponse(LoadConsumptionView view);
}
