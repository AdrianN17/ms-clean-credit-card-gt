package com.bank.credit_bank.application.consumption.port.in;

import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;

import java.util.UUID;

@FunctionalInterface
public interface CardCancelConsumptionUseCase {
    UUID cancelConsumption(CardCancelConsumptionCommand cardCancelConsumptionCommand);
}
