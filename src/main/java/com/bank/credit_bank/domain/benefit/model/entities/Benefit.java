package com.bank.credit_bank.domain.benefit.model.entities;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.benefit.events.BenefitClosedEvent;
import com.bank.credit_bank.domain.benefit.events.BenefitCreatedEvent;
import com.bank.credit_bank.domain.benefit.events.BenefitUpdatedEvent;
import com.bank.credit_bank.domain.benefit.model.exceptions.BenefitException;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;
import com.bank.credit_bank.domain.benefit.model.vo.DiscountPolicy;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.benefit.model.constants.BenefitConstant.*;
import static com.bank.credit_bank.domain.benefit.model.constants.BenefitErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class Benefit extends AggregateRoot<BenefitId> {
    private Point totalPoints;
    private final DiscountPolicy discountPolicy;
    private final CardId cardId;

    private Benefit(BenefitBuilder builder) {
        super(builder.id, builder.status, builder.createdDate, builder.updatedDate);
        this.totalPoints = builder.totalPoints;
        this.discountPolicy = builder.discountPolicy;
        this.cardId = builder.cardId;
        addCreatedEvent();
    }

    public Point getTotalPoints() {
        return totalPoints;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public CardId getCardId() {
        return cardId;
    }

    public void close() {
        softDelete();
        addClosedEvent();
    }

    public void accumulate(Amount amount, BigDecimal ratio) {

        isNotNull(amount, new BenefitException(AMOUNT_NOT_NULL));

        Integer pointEarned = amount.getAmount().divide(ratio, RoundingMode.DOWN).intValue();

        this.totalPoints = getTotalPoints()
                .earnPoints(Point.create(pointEarned));

        addUpdatedEvent();
    }

    public Amount discount(Amount amount, Point usedPoints) {
        isNotNull(amount, new BenefitException(PAYMENT_NOT_NULL));
        isNotNull(usedPoints, new BenefitException(POINT_NOT_NULL));

        isNotConditional(getTotalPoints()
                .calculateIfHaveEnoughPoints(usedPoints), new BenefitException(NOT_ENOUGH_POINTS));

        Point calculatePoints = (getDiscountPolicy().getHasDiscount()) ?
                usedPoints.mulitply(getDiscountPolicy().getMultiplierPoints()) :
                usedPoints;

        BigDecimal discount = new BigDecimal(calculatePoints.getPointEarned()).multiply(DISCOUNT_PER_POINT);

        this.totalPoints = getTotalPoints().dismissPoints(calculatePoints);

        addUpdatedEvent();

        return amount.descuento(discount);
    }

    private void addCreatedEvent() {
        addEvent(new BenefitCreatedEvent(
                id.getValue(),
                cardId.getValue(),
                totalPoints.getPointEarned(),
                discountPolicy.getHasDiscount(),
                discountPolicy.getMultiplierPoints()
        ));
    }

    private void addUpdatedEvent() {
        addEvent(new BenefitUpdatedEvent(
                id.getValue(),
                cardId.getValue(),
                totalPoints.getPointEarned(),
                discountPolicy.getHasDiscount(),
                discountPolicy.getMultiplierPoints()
        ));
    }

    private void addClosedEvent() {
        addEvent(new BenefitClosedEvent(
                id.getValue()
        ));
    }

    public static BenefitBuilder builder() {
        return new BenefitBuilder();
    }

    public static class BenefitBuilder {
        private BenefitId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private Point totalPoints;
        private DiscountPolicy discountPolicy;
        private CardId cardId;

        public BenefitBuilder benefitId(Long benefitId) {
            this.id = BenefitId.create(benefitId);
            return this;
        }

        public BenefitBuilder cardId(Long cardId) {
            this.cardId = CardId.create(cardId);
            return this;
        }

        public BenefitBuilder totalPoints(Integer totalPoints) {
            this.totalPoints = Point.create(totalPoints != null ? totalPoints : 0);
            return this;
        }

        public BenefitBuilder discountPolicy(Boolean hasDiscount, BigDecimal multiplierPoints) {
            isNotNull(hasDiscount, new BenefitException(DISCUOUNT_POLICY_NOT_NULL));
            this.discountPolicy = DiscountPolicy.create(hasDiscount, multiplierPoints);
            return this;
        }

        public BenefitBuilder status(Integer status) {
            this.status = StatusEnum.ofValue(status).orElseThrow();
            return this;
        }

        public BenefitBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public BenefitBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public Benefit build() {
            isNotNull(id, new BenefitException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BenefitException(CARD_ID_NOT_NULL));
            isNotNull(discountPolicy, new BenefitException(DISCUOUNT_POLICY_NOT_NULL));

            if (this.totalPoints == null) this.totalPoints = Point.create();
            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();

            return new Benefit(this);
        }
    }
}
