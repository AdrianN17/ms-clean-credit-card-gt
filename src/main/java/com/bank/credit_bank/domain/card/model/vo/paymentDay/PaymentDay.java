package com.bank.credit_bank.domain.card.model.vo.paymentDay;

import static com.bank.credit_bank.domain.card.model.vo.paymentDay.PaymentDayErrorMessage.PAYMENT_DAY_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class PaymentDay {
    private final Short paymentDay;

    public PaymentDay(Short paymentDay) {
        this.paymentDay = paymentDay;
    }

    public Short getValue() {
        return paymentDay;
    }

    public static PaymentDay create(Short paymentDay) {
        isNotNull(paymentDay, new PaymentDayException(PAYMENT_DAY_CANNOT_BE_NULL));
        return new PaymentDay(paymentDay);
    }
}
