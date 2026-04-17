package com.bank.credit_bank.application.business.card;

import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;

public interface BusinessServiceCard {
    Card get(Long cardId);
    CardId save(Card card);
}
