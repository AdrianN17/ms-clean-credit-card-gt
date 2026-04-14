package com.bank.credit_bank.domain.benefit.model.vo;

import com.bank.credit_bank.domain.benefit.model.exceptions.BenefitIdException;

import static com.bank.credit_bank.domain.benefit.model.constants.BenefitErrorMessage.BENEFIT_ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.benefit.model.constants.BenefitErrorMessage.BENEFIT_ID_MUST_BE_NUMERIC;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class BenefitId {
    private final Long value;

    public BenefitId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static BenefitId create(Long value) {
        isNotNull(value, new BenefitIdException(BENEFIT_ID_CANNOT_BE_NULL));
        return new BenefitId(value);
    }

    public static BenefitId create(String value) {
        isNotNull(value, new BenefitIdException(BENEFIT_ID_CANNOT_BE_NULL));
        isNotConditional(isNotLongData(value), new BenefitIdException(BENEFIT_ID_MUST_BE_NUMERIC));
        return new BenefitId(Long.valueOf(value));
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

