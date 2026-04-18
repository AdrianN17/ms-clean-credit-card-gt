package com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.projection;

import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.CardPersistanceException;

import static com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.CardErrorMessage.INCORRECT_CURRENCY_VALUE;

public interface CardCurrencyProjection {

    Integer getCurrency();

    default CurrencyEnum getCurrencyEnum() {
        return CurrencyEnum.ofValue(getCurrency())
                .orElseThrow(() -> new CardPersistanceException(INCORRECT_CURRENCY_VALUE));
    }
}
