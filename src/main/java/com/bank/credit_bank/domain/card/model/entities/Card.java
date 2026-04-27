package com.bank.credit_bank.domain.card.model.entities;

import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.events.CardClosedEvent;
import com.bank.credit_bank.domain.card.events.CardCreatedEvent;
import com.bank.credit_bank.domain.card.events.CardUpdatedEvent;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;
import com.bank.credit_bank.domain.card.model.exceptions.CardException;
import com.bank.credit_bank.domain.card.model.vo.cardAccountId.CardAccountId;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.card.model.vo.creditId.Credit;
import com.bank.credit_bank.domain.card.model.vo.paymentDay.PaymentDay;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.benefit.model.constants.BenefitConstant.*;
import static com.bank.credit_bank.domain.card.model.constants.CardErrorMessage.*;
import static com.bank.credit_bank.domain.card.model.enums.CardStatusEnum.IN_DEBT;
import static com.bank.credit_bank.domain.card.model.enums.CardStatusEnum.OPERATIVE;
import static com.bank.credit_bank.domain.card.model.vo.cardAccountId.CardAccountIdErrorMessage.CARD_ACCOUNTID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class Card extends AggregateRoot<CardId> {
    private final TypeCardEnum typeCard;
    private final CategoryCardEnum categoryCard;
    private final Credit credit;
    private CardStatusEnum cardStatus;
    private final CardAccountId cardAccountId;
    private final PaymentDay paymentDay;

    private Card(CardBuilder builder) {
        super(builder.id, builder.status, builder.createdDate, builder.updatedDate);
        this.typeCard = builder.typeCard;
        this.categoryCard = builder.categoryCard;
        this.credit = builder.credit;
        this.cardAccountId = builder.cardAccountId;
        this.paymentDay = builder.paymentDay;
        this.cardStatus = builder.cardStatus;

        addCreatedEvent();
    }

    public TypeCardEnum getTypeCard() {
        return typeCard;
    }

    public CategoryCardEnum getCategoryCard() {
        return categoryCard;
    }

    public Credit getCredit() {
        return credit;
    }

    public CardStatusEnum getCardStatus() {
        return cardStatus;
    }

    public CardAccountId getCardAccountId() {
        return cardAccountId;
    }

    public PaymentDay getPaymentDay() {
        return paymentDay;
    }

    public void updateStatus(Boolean isOvercharged) {
        if (isOvercharged)
            this.cardStatus = CardStatusEnum.OVERCHARGE;
        else
            this.cardStatus = OPERATIVE;

        addUpdatedEvent();
    }

    public void validateIfCardIfInDebt() {
        isNotConditional(Objects.equals(cardStatus, IN_DEBT), new CardException(IN_DEBT_CARD));
    }

    public BigDecimal getRatio() {
        return switch (categoryCard) {
            case NORMAL -> RATIO_NORMAL;
            case SILVER -> RATIO_SILVER;
            case GOLD -> RATIO_GOLD;
            case PLATINUM -> RATIO_PLATINUM;
            case BLACK -> RATIO_BLACK;
            case SIGNATURE -> RATIO_SIGNATURE;
            case INFINITY -> RATIO_INFINITY;
        };
    }

    public void close() {
        softDelete();
        addClosedEvent();
    }

    private void addCreatedEvent() {
        addEvent(
                new CardCreatedEvent(
                        getId().getValue(),
                        typeCard.getValue(),
                        categoryCard.getValue(),
                        cardAccountId.getValue(),
                        paymentDay.getValue(),
                        credit.getCreditTotal().getAmount(),
                        credit.getDebtTax(),
                        credit.getCreditTotal().getCurrency().getCurrency().getValue(),
                        credit.getCreditTotal().getCurrency().getExchangeRate(),
                        cardStatus.getValue()
                )
        );
    }

    private void addUpdatedEvent() {
        addEvent(
                new CardUpdatedEvent(
                        getId().getValue(),
                        cardStatus.getValue()
                )
        );
    }

    private void addClosedEvent() {
        addEvent(
                new CardClosedEvent(
                        getId().getValue()
                )
        );
    }

    public static CardBuilder builder() {
        return new CardBuilder();
    }

    public static class CardBuilder {
        private CardId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private TypeCardEnum typeCard;
        private CategoryCardEnum categoryCard;
        private Credit credit;
        private CardStatusEnum cardStatus;
        private CardAccountId cardAccountId;
        private PaymentDay paymentDay;

        // Raw primitive setters
        public CardBuilder cardId(Long cardId) {
            this.id = CardId.create(cardId);
            return this;
        }

        public CardBuilder typeCard(String typeCard) {
            this.typeCard = TypeCardEnum.ofCode(typeCard).orElseThrow(
                    () -> new CardException(TYPE_CARD_CANNOT_BE_NULL));
            return this;
        }

        public CardBuilder categoryCard(String categoryCard) {
            this.categoryCard = CategoryCardEnum.ofCode(categoryCard).orElseThrow(
                    () -> new CardException(CATEGORY_CARD_CANNOT_BE_NULL));
            return this;
        }

        public CardBuilder cardAccountId(Long cardAccountId) {
            this.cardAccountId = CardAccountId.create(cardAccountId);
            return this;
        }

        public CardBuilder paymentDay(Short paymentDay) {
            this.paymentDay = PaymentDay.create(paymentDay);
            return this;
        }

        public CardBuilder credit(BigDecimal creditTotal, BigDecimal debtTax, String currency, BigDecimal exchangeRate) {
            isNotNull(creditTotal, new CardException(CREDIT_TOTAL_CANNOT_BE_NULL));
            isNotNull(debtTax, new CardException(DEBT_TAX_CANNOT_BE_NULL));
            isNotNull(currency, new CardException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new CardException(EXCHANGE_RATE_CANNOT_BE_NULL));
            Currency cur = Currency.create(CurrencyEnum.ofCode(currency).orElseThrow(), exchangeRate);
            this.credit = Credit.create(Amount.create(cur, creditTotal), debtTax);
            return this;
        }

        public CardBuilder cardStatus(String cardStatus) {
            this.cardStatus = CardStatusEnum.ofCode(cardStatus).orElseThrow(
                    () -> new CardException(CARD_STATUS_CANNOT_BE_NULL));
            return this;
        }

        public CardBuilder status(Integer status) {
            this.status = StatusEnum.ofValue(status).orElseThrow();
            return this;
        }

        public CardBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public CardBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public Card build() {
            isNotNull(id, new CardException(ID_CANNOT_BE_NULL));
            isNotNull(typeCard, new CardException(TYPE_CARD_CANNOT_BE_NULL));
            isNotNull(categoryCard, new CardException(CATEGORY_CARD_CANNOT_BE_NULL));
            isNotNull(cardAccountId, new CardException(CARD_ACCOUNTID_CANNOT_BE_NULL));
            isNotNull(paymentDay, new CardException(PAYMENT_DAY_CANNOT_BE_NULL));
            isNotNull(credit, new CardException(CREDIT_CANNOT_BE_NULL));

            if (this.cardStatus == null) this.cardStatus = OPERATIVE;
            if (this.status == null) this.status = ACTIVE;

            return new Card(this);
        }
    }
}
