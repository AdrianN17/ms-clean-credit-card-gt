package com.bank.credit_bank.application.consumption.port.in;

import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

@FunctionalInterface
public interface ConsumptionProcessUseCase {
    ConsumptionId execute(CardProcessConsumptionCommand cardProcessConsumptionCommand);
}
