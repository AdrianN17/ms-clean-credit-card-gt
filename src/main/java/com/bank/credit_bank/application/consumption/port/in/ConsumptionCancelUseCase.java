package com.bank.credit_bank.application.consumption.port.in;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

@FunctionalInterface
public interface ConsumptionCancelUseCase {
    ConsumptionId execute(CardCancelConsumptionCommand cardCancelConsumptionCommand);
}
