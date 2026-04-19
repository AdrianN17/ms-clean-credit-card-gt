package com.bank.credit_bank.domain.benefit.model.vo;

import com.bank.credit_bank.domain.benefit.model.exceptions.BenefitIdException;

import static com.bank.credit_bank.domain.benefit.model.constants.BenefitIdErrorMessage.BENEFIT_ID_CANNOT_BE_NULL;
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
}

