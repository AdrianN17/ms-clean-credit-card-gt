package com.bank.credit_bank.application.consumption.port.in;

import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import java.util.List;

@FunctionalInterface
public interface ConsumptionSplitUseCase {
    List<ConsumptionId> execute(CardSplitConsumptionCommand cardSplitConsumptionCommand);
}
