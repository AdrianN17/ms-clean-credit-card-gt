package com.bank.credit_bank.application.card.port.in;

import com.bank.credit_card.application.port.in.command.CardCreateCommand;
import com.bank.credit_card.domain.card.Card;

@FunctionalInterface
public interface CardCreateUseCase {
    Card createCard(CardCreateCommand cardCreateCommand);
}
