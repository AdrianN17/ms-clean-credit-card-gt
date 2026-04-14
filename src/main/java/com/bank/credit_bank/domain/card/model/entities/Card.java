package com.bank.credit_bank.domain.card.model.entities;

import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.events.CardClosedEvent;
import com.bank.credit_bank.domain.card.events.CardCreatedEvent;
import com.bank.credit_bank.domain.card.events.CardUpdatedEvent;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;
import com.bank.credit_bank.domain.card.model.exceptions.CardException;
import com.bank.credit_bank.domain.card.model.vo.CardAccountId;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.card.model.vo.Credit;
import com.bank.credit_bank.domain.card.model.vo.PaymentDay;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.exceptions.ConsumptionException;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;
import com.bank.credit_bank.domain.payment.model.entities.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.*;
import static com.bank.credit_bank.domain.card.model.constants.CardErrorMessage.*;
import static com.bank.credit_bank.domain.card.model.enums.CardStatusEnum.IN_DEBT;
import static com.bank.credit_bank.domain.card.model.enums.CardStatusEnum.OPERATIVE;
import static com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum.ADELANTADO;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class Card extends AggregateRoot<CardId> {
    private final TypeCardEnum typeCard;
    private final CategoryCardEnum categoryCard;
    private final Credit credit;
    private CardStatusEnum cardStatus;
    private final CardAccountId cardAccountId;
    private final PaymentDay paymentDay;

    public Card(CardId id, StatusEnum status, LocalDateTime createdDate, LocalDateTime updatedDate, TypeCardEnum typeCard, CategoryCardEnum categoryCard, Credit credit, CardStatusEnum cardStatus, CardAccountId cardAccountId, PaymentDay paymentDay) {
        super(id, status, createdDate, updatedDate);
        this.typeCard = typeCard;
        this.categoryCard = categoryCard;
        this.credit = credit;
        this.cardAccountId = cardAccountId;
        this.paymentDay = paymentDay;
        this.cardStatus = cardStatus;

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

    public void pay(Balance balance,
                    Payment payment) {

        isNotNull(balance, new CardException(BALANCE_CANNOT_BE_NULL));

        isNotNull(payment, new CardException(PAYMENT_CANNOT_BE_NULL));

        if (Objects.equals(payment.getCategory(), ADELANTADO))
            balance.prePay(payment);
        else
            balance.pay(payment);

        updateStatus(balance);

    }

    public void pay(Balance balance, Benefit benefit, Payment payment, Point point) {

        isNotNull(balance, new CardException(BALANCE_CANNOT_BE_NULL));
        isNotNull(benefit, new CardException(BENEFIT_CANNOT_BE_NULL));

        isNotNull(payment, new CardException(PAYMENT_CANNOT_BE_NULL));
        isNotNull(point, new CardException(POINT_CANNOT_BE_NULL));

        isNotConditional(Objects.equals(payment.getCategory(), ADELANTADO), new CardException(POINTS_CANNOT_USED_WITH_PREPAY));

        balance.pay(benefit.discount(payment, point));

        updateStatus(balance);
    }

    private void updateStatus(Balance balance) {
        if (balance.isOvercharged())
            this.cardStatus = CardStatusEnum.OVERCHARGE;
        else
            this.cardStatus = OPERATIVE;

        addUpdatedEvent();
    }

    public void consumption(Balance balance, Benefit benefit, Consumption consumption) {

        isNotNull(balance, new CardException(BALANCE_CANNOT_BE_NULL));
        isNotNull(benefit, new CardException(BENEFIT_CANNOT_BE_NULL));

        isNotNull(consumption, new CardException(CONSUMPTION_CANNOT_BE_NULL));

        isNotConditional(Objects.equals(getCardStatus(), IN_DEBT), new CardException(IN_DEBT_CARD));

        Amount totalAvailable = balance.calculateConsumption(consumption.getConsumptionAmount());

        isNotConditional(totalAvailable.estaFaltando(balance.getTotal()), new ConsumptionException(AMOUNT_EXCEED_CREDIT_LIMIT));

        balance.applyConsumption(consumption.getConsumptionAmount());

        benefit.accumulate(consumption.getConsumptionAmount(), getCategoryCard());
    }

    public List<Consumption> split(Balance balance, Consumption consumption, Integer quantity) {
        isNotNull(balance, new CardException(BALANCE_CANNOT_BE_NULL));

        consumption.validateIfConsumptionIsInApprobation();
        List<Consumption> consumptionsSplit = consumption.split(quantity, credit.getDebtTax());
        balance.cancelConsumption(consumption);
        balance.consumptionSplitted(consumptionsSplit);
        return consumptionsSplit;
    }

    public void cancelConsumption(Balance balance, Consumption consumption) {

        isNotNull(balance, new CardException(BALANCE_CANNOT_BE_NULL));

        isNotNull(consumption, new CardException(CONSUMPTION_CANNOT_BE_NULL));
        consumption.validateIfConsumptionIsInApprobation();
        balance.cancelConsumption(consumption);
    }

    public void cancelPayment(Balance balance, Payment payment) {

        isNotNull(balance, new CardException(BALANCE_CANNOT_BE_NULL));

        isNotNull(payment, new CardException(PAYMENT_CANNOT_BE_NULL));
        payment.validateIfPaymentIsInApprobation();
        balance.cancelPayment(payment);
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
}


