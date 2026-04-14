package com.bank.credit_bank.domain.consumption.model.vo;

import com.bank.credit_bank.domain.consumption.model.exceptions.ConsumptionIdException;

import static com.bank.credit_bank.domain.consumption.model.Constants.ConsumptionIdErrorMessage.CONSUMPTION_ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.consumption.model.Constants.ConsumptionIdErrorMessage.CONSUMPTION_ID_MUST_BE_NUMERIC;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class ConsumptionId {
    private final Long value;

    public ConsumptionId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static ConsumptionId create(Long value) {
        isNotNull(value, new ConsumptionIdException(CONSUMPTION_ID_CANNOT_BE_NULL));
        return new ConsumptionId(value);
    }

    public static ConsumptionId create(String value) {
        isNotNull(value, new ConsumptionIdException(CONSUMPTION_ID_CANNOT_BE_NULL));
        isNotConditional(isNotLongData(value), new ConsumptionIdException(CONSUMPTION_ID_MUST_BE_NUMERIC));
        return new ConsumptionId(Long.valueOf(value));
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
