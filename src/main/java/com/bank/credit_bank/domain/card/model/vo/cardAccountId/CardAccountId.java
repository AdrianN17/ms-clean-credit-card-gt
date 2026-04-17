package com.bank.credit_bank.domain.card.model.vo.cardAccountId;

import com.bank.credit_bank.domain.card.model.vo.cardId.CardIdException;

import static com.bank.credit_bank.domain.card.model.vo.cardId.CardIdErrorMessage.CARD_ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class CardAccountId {
    private final Long value;

    public CardAccountId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static CardAccountId create(Long value) {
        isNotNull(value, new CardIdException(CARD_ID_CANNOT_BE_NULL));
        return new CardAccountId(value);
    }
}
