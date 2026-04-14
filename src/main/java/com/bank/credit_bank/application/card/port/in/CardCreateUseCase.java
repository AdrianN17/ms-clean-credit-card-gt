package com.bank.credit_bank.application.card.port.in;


import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.domain.card.model.vo.CardId;

@FunctionalInterface
public interface CardCreateUseCase {
    CardId createCard(CardCreateCommand cardCreateCommand);
}
