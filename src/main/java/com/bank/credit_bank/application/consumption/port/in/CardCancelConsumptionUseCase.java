package com.bank.credit_bank.application.consumption.port.in;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.domain.card.model.vo.CardId;

@FunctionalInterface
public interface CardCancelConsumptionUseCase {
    CardId cancelConsumption(CardCancelConsumptionCommand cardCancelConsumptionCommand);
}
