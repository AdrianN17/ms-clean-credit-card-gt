package com.bank.credit_bank.application.card.port.out;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;

import java.util.Optional;

@FunctionalInterface
public interface CardDBSavePort {
    Optional<CardId> save(CardRequestDto card);
}
