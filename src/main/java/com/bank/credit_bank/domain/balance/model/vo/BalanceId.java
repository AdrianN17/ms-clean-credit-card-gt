package com.bank.credit_bank.domain.balance.model.vo;

import com.bank.credit_bank.domain.balance.model.exceptions.BalanceIdException;

import static com.bank.credit_bank.domain.balance.model.constants.BalanceIdErrorMessage.BALANCE_ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class BalanceId {
    private final Long value;

    public BalanceId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static BalanceId create(Long value) {
        isNotNull(value, new BalanceIdException(BALANCE_ID_CANNOT_BE_NULL));
        return new BalanceId(value);
    }
}
