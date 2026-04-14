package com.bank.credit_bank.domain.benefit.model.factory;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;
import com.bank.credit_bank.domain.benefit.model.exceptions.BenefitException;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;
import com.bank.credit_bank.domain.benefit.model.vo.DiscountPolicy;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.generic.exceptions.EntityException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.benefit.model.constants.BenefitErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class BenefitFactoryImpl implements BenefitFactory {

    @Override
    public Benefit create(Long benefitId,
                          Long cardId,
                          Integer totalPoints,
                          Boolean hasDiscount,
                          BigDecimal multiplierPoints,
                          Integer status,
                          LocalDateTime createdDate,
                          LocalDateTime updatedDate) {
        try {
            isNotNull(benefitId, new BenefitException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BenefitException(CARD_ID_NOT_NULL));
            isNotNull(totalPoints, new BenefitException(POINT_NOT_NULL));
            isNotNull(hasDiscount, new BenefitException(DISCUOUNT_POLICY_NOT_NULL));

            return new Benefit(
                    BenefitId.create(benefitId),
                    StatusEnum.ofValue(status).orElseThrow(),
                    createdDate,
                    updatedDate,
                    Point.create(totalPoints),
                    DiscountPolicy.create(hasDiscount, multiplierPoints),
                    CardId.create(cardId)
            );
        } catch (EntityException e) {
            throw new BenefitException(e.getMessage());
        }
    }

    @Override
    public Benefit create(Long benefitId,
                          Long cardId,
                          Boolean hasDiscount,
                          BigDecimal multiplierPoints) {
        try {
            isNotNull(benefitId, new BenefitException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BenefitException(CARD_ID_NOT_NULL));
            isNotNull(hasDiscount, new BenefitException(DISCUOUNT_POLICY_NOT_NULL));

            return new Benefit(
                    BenefitId.create(benefitId),
                    ACTIVE,
                    LocalDateTime.now(),
                    null,
                    Point.create(),
                    DiscountPolicy.create(hasDiscount, multiplierPoints),
                    CardId.create(cardId)
            );
        } catch (EntityException e) {
            throw new BenefitException(e.getMessage());
        }
    }
}

