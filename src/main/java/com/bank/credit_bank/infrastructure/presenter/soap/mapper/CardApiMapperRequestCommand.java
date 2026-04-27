package com.bank.credit_bank.infrastructure.presenter.soap.mapper;

import com.bank.credit_bank.application.card.commands.CardCloseCommand;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ControlCardRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.InitiateCardRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.CardResponse;

public interface CardApiMapperRequestCommand {
    CardCreateCommand toCommand(InitiateCardRequest request);

    CardCloseCommand toCommandId(ControlCardRequest parameters);

    CardResponse toResponse(LoadCardBalanceBenefitView view);
}
