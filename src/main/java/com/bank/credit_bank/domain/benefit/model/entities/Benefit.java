package com.bank.credit_bank.domain.benefit.model.entities;

import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.benefit.events.BenefitClosedEvent;
import com.bank.credit_bank.domain.benefit.events.BenefitCreatedEvent;
import com.bank.credit_bank.domain.benefit.events.BenefitUpdatedEvent;
import com.bank.credit_bank.domain.benefit.model.exceptions.BenefitException;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;
import com.bank.credit_bank.domain.benefit.model.vo.DiscountPolicy;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;
import com.bank.credit_bank.domain.payment.model.entities.Payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static com.bank.credit_bank.domain.benefit.model.constants.BenefitConstant.*;
import static com.bank.credit_bank.domain.benefit.model.constants.BenefitErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class Benefit extends AggregateRoot<BenefitId> {
    private Point totalPoints;
    private final DiscountPolicy discountPolicy;
    private final CardId cardId;

    public Benefit(BenefitId id,
                   StatusEnum status,
                   LocalDateTime createdDate,
                   LocalDateTime updatedDate,
                   Point totalPoints,
                   DiscountPolicy discountPolicy,
                   CardId cardId) {
        super(id, status, createdDate, updatedDate);
        this.totalPoints = totalPoints;
        this.discountPolicy = discountPolicy;
        this.cardId = cardId;
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

    public void accumulate(Amount amount,
                           CategoryCardEnum categoryCard) {

        isNotNull(amount, new BenefitException(AMOUNT_NOT_NULL));
        isNotNull(categoryCard, new BenefitException(CATEGORY_NOT_NULL));

        BigDecimal ratio = switch (categoryCard) {
            case NORMAL -> RATIO_NORMAL;
            case SILVER -> RATIO_SILVER;
            case GOLD -> RATIO_GOLD;
            case PLATINUM -> RATIO_PLATINUM;
            case BLACK -> RATIO_BLACK;
            case SIGNATURE -> RATIO_SIGNATURE;
            case INFINITY -> RATIO_INFINITY;
        };

        Integer pointEarned = amount.getAmount().divide(ratio, RoundingMode.DOWN).intValue();

        this.totalPoints = getTotalPoints()
                .earnPoints(Point.create(pointEarned));

        addUpdatedEvent();
    }

    public Payment discount(Payment payment,
                            Point puntosUsar) {
        isNotNull(payment, new BenefitException(PAYMENT_NOT_NULL));
        isNotNull(puntosUsar, new BenefitException(POINT_NOT_NULL));

        isNotConditional(getTotalPoints()
                .calculateIfHaveEnoughPoints(puntosUsar), new BenefitException(NOT_ENOUGH_POINTS));

        Point calculatePoints = (getDiscountPolicy().getHasDiscount()) ?
                puntosUsar.mulitply(getDiscountPolicy().getMultiplierPoints()) :
                puntosUsar;

        BigDecimal discount = new BigDecimal(calculatePoints.getPointEarned()).multiply(DISCOUNT_PER_POINT);

        this.totalPoints = getTotalPoints().dismissPoints(calculatePoints);

        addUpdatedEvent();

        return payment.discount(discount);
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
}
