package com.bank.credit_bank.application.consumption.commands;

import java.util.UUID;

public record CardCancelConsumptionCommand(UUID consumptionId, Long cardId) {
}
