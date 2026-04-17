package com.bank.credit_bank.domain.balance.model.entities;

import com.bank.credit_bank.domain.balance.events.BalanceClosedEvent;
import com.bank.credit_bank.domain.balance.events.BalanceCreatedEvent;
import com.bank.credit_bank.domain.balance.events.BalanceUpdatedEvent;
import com.bank.credit_bank.domain.balance.model.exceptions.BalanceException;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Approbation;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.bank.credit_bank.domain.balance.model.constants.BalanceConstant.OVERCHARGE_LIMIT;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.*;
import static com.bank.credit_bank.domain.base.constants.DateRangeErrorMessage.DATE_NOT_WITHIN_RANGE;
import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.card.model.enums.CardStatusEnum.IN_DEBT;
import static com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum.ADELANTADO;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class Balance extends AggregateRoot<BalanceId> {
    private final CardId cardId;
    private final Amount total;
    private final Amount old;
    private final DateRange dateRange;
    private Amount available;

    private Balance(BalanceBuilder builder) {
        super(builder.id, builder.status, builder.createdDate, builder.updatedDate);
        this.cardId = builder.cardId;
        this.total = builder.total;
        this.old = builder.old;
        this.dateRange = builder.dateRange;
        this.available = builder.available;
        addCreatedEvent();
    }

    public CardId getCardId() {
        return cardId;
    }

    public Amount getTotal() {
        return total;
    }

    public Amount getOld() {
        return old;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public Amount getAvailable() {
        return available;
    }

    public void close() {
        softDelete();
        addClosedEvent();
    }

    private Boolean unavailablePayment(LocalDateTime fecha) {
        return getDateRange().ensureWithinRange(fecha.toLocalDate());
    }

    private Amount calculatePayment(Amount payment) {
        return getAvailable().mas(payment);
    }

    private void applyPayment(Amount payment) {
        this.available = getAvailable().mas(payment);
        addUpdatedEvent();
    }

    private Amount calculateConsumption(Amount consumption) {
        return getAvailable().menos(consumption);
    }

    private void applyConsumption(Amount consumption) {
        this.available = getAvailable().menos(consumption);
        addUpdatedEvent();
    }

    private void applyCancelledPayment(Amount payment) {
        this.available = getAvailable().menos(payment);
        addUpdatedEvent();
    }

    private void applyCancelledConsumption(Amount consumption) {
        this.available = getAvailable().mas(consumption);
        addUpdatedEvent();
    }

    public Boolean isOvercharged() {
        BigDecimal limitOvercharge = getTotal().getAmount().multiply(OVERCHARGE_LIMIT);
        BigDecimal totalLimit = getTotal().getAmount().add(limitOvercharge);
        return getAvailable().getAmount().compareTo(totalLimit) > 0;
    }

    public void consumeBalance(Amount consumption, CardStatusEnum cardStatus) {
        isNotConditional(Objects.equals(cardStatus, IN_DEBT), new BalanceException(IN_DEBT_CARD));
        Amount totalAvailable = calculateConsumption(consumption);
        isNotConditional(totalAvailable.estaFaltando(getTotal()), new BalanceException(AMOUNT_EXCEED_CREDIT_LIMIT));
        applyConsumption(consumption);
    }

    public void payBalance(Amount payment, CategoryPaymentEnum categoryPayment, Approbation approbation) {
        if (Objects.equals(categoryPayment, ADELANTADO))
            prePay(payment, categoryPayment);
        else
            pay(payment, categoryPayment, approbation);
    }

    private void prePay(Amount payment, CategoryPaymentEnum categoryPayment) {
        isNotNull(payment, new BalanceException(PAYMENT_CANNOT_BE_NULL));
        isNotConditional(!Objects.equals(categoryPayment, ADELANTADO), new BalanceException(DATE_NOT_WITHIN_RANGE));
        applyPayment(payment);
        Amount totalAmount = getAvailable();
        Amount totalBalance = getTotal();
        isNotConditional(totalBalance.estaSobrando(totalAmount),
                new BalanceException(PAYMENT_CATEGORY_EXCEED_LIKE + totalBalance.menos(totalAmount).toString()));
    }

    private void pay(Amount payment, CategoryPaymentEnum categoryPayment, Approbation approbation) {
        isNotNull(payment, new BalanceException(PAYMENT_CANNOT_BE_NULL));
        isNotConditional(unavailablePayment(approbation.getDate()), new BalanceException(DATE_NOT_WITHIN_RANGE));
        Amount totalAmount = getAvailable();
        Amount totalBalance = getTotal();
        isNotConditional(totalBalance.esIgual(totalAmount) && !Objects.equals(categoryPayment, CategoryPaymentEnum.TOTAL),
                new BalanceException(PAYMENT_CATEGORY_NOT_SAME_AS_PAYMENT));
        isNotConditional(totalAmount.estaSobrando(totalBalance),
                new BalanceException(PAYMENT_CATEGORY_EXCEED_LIKE + totalAmount.menos(totalBalance).toString()));
        applyPayment(payment);
    }

    public void cancelConsumption(Amount consumption) {
        isNotNull(consumption, new BalanceException(CONSUMPTION_CANNOT_BE_NULL));
        applyCancelledConsumption(consumption);
    }

    public void cancelPayment(Amount payment) {
        isNotNull(payment, new BalanceException(PAYMENT_CANNOT_BE_NULL));
        applyCancelledPayment(payment);
    }

    private void addCreatedEvent() {
        addEvent(new BalanceCreatedEvent(
                id.getValue(), cardId.getValue(),
                total.getAmount(), old.getAmount(), available.getAmount(),
                dateRange.getStartDate(), dateRange.getEndDate()
        ));
    }

    private void addUpdatedEvent() {
        addEvent(new BalanceUpdatedEvent(id.getValue(), cardId.getValue(), available.getAmount()));
    }

    private void addClosedEvent() {
        addEvent(new BalanceClosedEvent(id.getValue()));
    }

    public static BalanceBuilder builder() {
        return new BalanceBuilder();
    }

    public static class BalanceBuilder {
        private BalanceId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private CardId cardId;
        private Amount total;
        private Amount old;
        private Amount available;
        private DateRange dateRange;

        // Raw currency fields for building Amounts
        private CurrencyEnum currencyEnum;
        private BigDecimal exchangeRate;

        public BalanceBuilder balanceId(Long balanceId) {
            this.id = BalanceId.create(balanceId);
            return this;
        }

        public BalanceBuilder cardId(Long cardId) {
            this.cardId = CardId.create(cardId);
            return this;
        }

        public BalanceBuilder currency(Integer currency, BigDecimal exchangeRate) {
            isNotNull(currency, new BalanceException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new BalanceException(EXCHANGE_RATE_CANNOT_BE_NULL));
            this.currencyEnum = CurrencyEnum.ofValue(currency).orElseThrow();
            this.exchangeRate = exchangeRate;
            return this;
        }

        public BalanceBuilder total(BigDecimal total) {
            isNotNull(total, new BalanceException(TOTAL_AMOUNT_CANNOT_BE_NULL));
            this.total = Amount.create(Currency.create(currencyEnum, exchangeRate), total);
            return this;
        }

        public BalanceBuilder old(BigDecimal old) {
            isNotNull(old, new BalanceException(OLD_AMOUNT_CANNOT_BE_NULL));
            this.old = Amount.create(Currency.create(currencyEnum, exchangeRate), old);
            return this;
        }

        public BalanceBuilder available(BigDecimal available) {
            isNotNull(available, new BalanceException(AVAILABLE_AMOUNT_CANNOT_BE_NULL));
            this.available = Amount.create(Currency.create(currencyEnum, exchangeRate), available);
            return this;
        }

        public BalanceBuilder dateRange(Short paymentDay) {
            isNotNull(paymentDay, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            this.dateRange = DateRange.create(paymentDay);
            return this;
        }

        public BalanceBuilder dateRange(LocalDate startDate, LocalDate endDate) {
            isNotNull(startDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            isNotNull(endDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            this.dateRange = DateRange.create(startDate, endDate);
            return this;
        }

        public BalanceBuilder status(Integer status) {
            this.status = StatusEnum.ofValue(status).orElseThrow();
            return this;
        }

        public BalanceBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public BalanceBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public Balance build() {
            isNotNull(id, new BalanceException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BalanceException(CARD_ID_CANNOT_BE_NULL));
            isNotNull(total, new BalanceException(TOTAL_AMOUNT_CANNOT_BE_NULL));
            isNotNull(dateRange, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));

            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();

            // Si no se especificaron old y available, se usan el valor de total (creación nueva)
            if (this.old == null) this.old = this.total;
            if (this.available == null) this.available = this.total;

            return new Balance(this);
        }
    }
}
