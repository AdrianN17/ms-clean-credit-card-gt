package com.bank.credit_bank.application.payment.commands;

import java.util.UUID;

public record CardCancelPaymentCommand(UUID paymentId, Long cardId) {
}
