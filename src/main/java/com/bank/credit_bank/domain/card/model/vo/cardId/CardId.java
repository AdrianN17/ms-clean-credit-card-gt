package com.bank.credit_bank.domain.card.model.vo.cardId;

import static com.bank.credit_bank.domain.card.model.vo.cardId.CardIdErrorMessage.CARD_ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.card.model.vo.cardId.CardIdErrorMessage.CARD_ID_MUST_BE_NUMERIC;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public final class CardId {
    private final Long value;

    public CardId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static CardId create(Long value) {
        isNotNull(value, new CardIdException(CARD_ID_CANNOT_BE_NULL));
        return new CardId(value);
    }

    public static CardId create(String value) {
        isNotNull(value, new CardIdException(CARD_ID_CANNOT_BE_NULL));
        isNotConditional(isNotLongData(value), new CardIdException(CARD_ID_MUST_BE_NUMERIC));
        return new CardId(Long.valueOf(value));
    }

    public static boolean isNotLongData(String valor) {
        try {
            Long.parseLong(valor);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
