package com.bank.credit_bank.domain.benefit.model.vo;

import com.bank.credit_bank.domain.benefit.model.exceptions.DiscountPolicyException;

import java.math.BigDecimal;

import static com.bank.credit_bank.domain.benefit.model.constants.DiscountPolicyErrorMessage.HAS_DISCOUNT_NOT_NULL;
import static com.bank.credit_bank.domain.benefit.model.constants.DiscountPolicyErrorMessage.MULTIPLIER_POINTS_NOT_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class DiscountPolicy {
    private final Boolean hasDiscount;
    private final BigDecimal multiplierPoints;

    private DiscountPolicy(Boolean hasDiscount, BigDecimal multiplierPoints) {
        this.hasDiscount = hasDiscount;
        this.multiplierPoints = multiplierPoints;
    }

    public static DiscountPolicy create(Boolean hasDiscount, BigDecimal multiplierPoints) {

        isNotNull(hasDiscount, new DiscountPolicyException(HAS_DISCOUNT_NOT_NULL));
        if (hasDiscount)
            isNotNull(multiplierPoints, new DiscountPolicyException(MULTIPLIER_POINTS_NOT_NULL));

        return new DiscountPolicy(hasDiscount, multiplierPoints);
    }

    public Boolean getHasDiscount() {
        return hasDiscount;
    }

    public BigDecimal getMultiplierPoints() {
        return multiplierPoints;
    }
}
