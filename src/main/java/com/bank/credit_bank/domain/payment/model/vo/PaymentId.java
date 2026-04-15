package com.bank.credit_bank.domain.payment.model.vo;

import com.bank.credit_bank.domain.payment.model.exceptions.PaymentException;

import java.util.UUID;

import static com.bank.credit_bank.domain.payment.model.constants.PaymentErrorMessage.IDENTIFIER_ID_NOT_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class PaymentId {
    private final UUID value;

    public PaymentId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public static PaymentId create(UUID value) {
        isNotNull(value, new PaymentException(IDENTIFIER_ID_NOT_NULL));
        return new PaymentId(value);
    }
}
