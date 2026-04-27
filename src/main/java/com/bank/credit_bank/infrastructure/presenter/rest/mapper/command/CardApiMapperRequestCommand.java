package com.bank.credit_bank.infrastructure.presenter.rest.mapper.command;

import com.bank.credit_bank.application.card.commands.CardCloseCommand;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.CardRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.CardResponse;

public interface CardApiMapperRequestCommand {
    CardCreateCommand toCommand(CardRequest request);
    CardCloseCommand toCommandId(Long cardId);
    CardResponse toResponse(LoadCardBalanceBenefitView view);
}
