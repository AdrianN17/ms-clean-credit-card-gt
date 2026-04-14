package com.bank.credit_bank.domain.payment.model.vo;

import com.bank.credit_bank.domain.payment.model.exceptions.PaymentException;

import static com.bank.credit_bank.domain.payment.model.constants.PaymentErrorMessage.IDENTIFIER_ID_NOT_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class PaymentId {
    private final Long value;

    public PaymentId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static PaymentId create(Long value) {
        isNotNull(value, new PaymentException(IDENTIFIER_ID_NOT_NULL));
        return new PaymentId(value);
    }

    public static PaymentId create(String value) {
        isNotNull(value, new PaymentException(IDENTIFIER_ID_NOT_NULL));
        isNotConditional(isNotLongData(value), new PaymentException(IDENTIFIER_ID_NOT_NULL));
        return new PaymentId(Long.valueOf(value));
    }

    private static boolean isNotLongData(String value) {
        try {
            Long.parseLong(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
