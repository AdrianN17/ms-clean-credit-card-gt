package com.bank.credit_bank.application.consumption.commands;

import java.util.UUID;

public record CardSplitConsumptionCommand(
        UUID consumptionId,
        Integer installments,
        Long cardId) {
}
