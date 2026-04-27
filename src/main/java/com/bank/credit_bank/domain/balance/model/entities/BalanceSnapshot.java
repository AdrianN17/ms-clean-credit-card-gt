package com.bank.credit_bank.domain.balance.model.entities;

import com.bank.credit_bank.domain.balance.events.BalanceClosedEvent;
import com.bank.credit_bank.domain.balance.model.exceptions.BalanceException;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;
import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.*;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.AVAILABLE_AMOUNT_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.CARD_ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.DATE_RANGE_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.ID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.OLD_AMOUNT_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.TOTAL_AMOUNT_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class BalanceSnapshot extends AggregateRoot<BalanceId> implements Balance {

    private final CardId cardId;
    private final Amount total;
    private final Amount old;
    private final Amount available;
    private final DateRange dateRange;

    public BalanceSnapshot(BalanceId id,
                           StatusEnum status,
                           LocalDateTime createdDate,
                           LocalDateTime updatedDate,
                           CardId cardId,
                           Amount total,
                           Amount old,
                           DateRange dateRange,
                           Amount available) {
        super(id, status, createdDate, updatedDate);
        this.cardId = cardId;
        this.total = total;
        this.old = old;
        this.available = available;
        this.dateRange = dateRange;
    }

    public static BalanceSnapshot of(BalanceId id, StatusEnum status,
                                     LocalDateTime createdDate, LocalDateTime updatedDate,
                                     CardId cardId, Amount total, Amount old,
                                     DateRange dateRange, Amount available) {
        return new BalanceSnapshot(id, status, createdDate, updatedDate, cardId, total, old, dateRange, available);
    }

    @Override public BalanceId getId()              { return (BalanceId) super.getId(); }
    @Override public CardId getCardId()             { return cardId; }
    @Override public Amount getTotal()              { return total; }
    @Override public Amount getOld()                { return old; }
    @Override public Amount getAvailable()          { return available; }
    @Override public DateRange getDateRange()       { return dateRange; }
    @Override public StatusEnum getStatus()         { return super.getStatus(); }
    @Override public LocalDateTime getCreatedDate() { return super.getCreatedDate(); }
    @Override public LocalDateTime getUpdatedDate() { return super.getUpdatedDate(); }

    @Override
    public List<DomainEvent> pullDomainEvents() {
        return super.pullDomainEvents();
    }

    @Override
    public void close() {
        softDelete();
        addClosedEvent();
    }

    private void addClosedEvent() {
        addEvent(new BalanceClosedEvent(id.getValue()));
    }

    public static BalanceSnapshot.BalanceSnapshotBuilder builder() {
        return new BalanceSnapshot.BalanceSnapshotBuilder();
    }

    public static BalanceSnapshot from(Balance balance) {
        return BalanceSnapshot.builder()
                .balanceId(balance.getId().getValue())
                .status(balance.getStatus().getValue())
                .createdDate(balance.getCreatedDate())
                .updatedDate(balance.getUpdatedDate())
                .currency(balance.getTotal().getCurrency().getCurrency().getCode(),
                        balance.getTotal().getCurrency().getExchangeRate())
                .cardId(balance.getCardId().getValue())
                .total(balance.getTotal().getAmount())
                .old(balance.getOld().getAmount())
                .available(balance.getAvailable().getAmount())
                .dateRange(balance.getDateRange().getStartDate(), balance.getDateRange().getEndDate())
                .build();
    }

    public static class BalanceSnapshotBuilder {
        private BalanceId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private CardId cardId;
        private Amount total;
        private Amount old;
        private Amount available;
        private DateRange dateRange;

        private CurrencyEnum currencyEnum;
        private BigDecimal exchangeRate;

        public BalanceSnapshot.BalanceSnapshotBuilder balanceId(Long balanceId) {
            this.id = BalanceId.create(balanceId);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder cardId(Long cardId) {
            this.cardId = CardId.create(cardId);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder currency(String currency, BigDecimal exchangeRate) {
            isNotNull(currency, new BalanceException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new BalanceException(EXCHANGE_RATE_CANNOT_BE_NULL));
            this.currencyEnum = CurrencyEnum.ofCode(currency).orElseThrow();
            this.exchangeRate = exchangeRate;
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder total(BigDecimal total) {
            isNotNull(total, new BalanceException(TOTAL_AMOUNT_CANNOT_BE_NULL));
            this.total = Amount.create(Currency.create(currencyEnum, exchangeRate), total);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder old(BigDecimal old) {
            isNotNull(old, new BalanceException(OLD_AMOUNT_CANNOT_BE_NULL));
            this.old = Amount.create(Currency.create(currencyEnum, exchangeRate), old);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder available(BigDecimal available) {
            isNotNull(available, new BalanceException(AVAILABLE_AMOUNT_CANNOT_BE_NULL));
            this.available = Amount.create(Currency.create(currencyEnum, exchangeRate), available);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder dateRange(Short paymentDay) {
            isNotNull(paymentDay, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            this.dateRange = DateRange.create(paymentDay);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder dateRange(LocalDate startDate, LocalDate endDate) {
            isNotNull(startDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            isNotNull(endDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            this.dateRange = DateRange.create(startDate, endDate);
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder status(Integer status) {
            this.status = StatusEnum.ofValue(status).orElseThrow();
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public BalanceSnapshot.BalanceSnapshotBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public BalanceSnapshot build() {
            isNotNull(id, new BalanceException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BalanceException(CARD_ID_CANNOT_BE_NULL));
            isNotNull(total, new BalanceException(TOTAL_AMOUNT_CANNOT_BE_NULL));
            isNotNull(dateRange, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));

            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();

            if (this.old == null) this.old = this.total;
            if (this.available == null) this.available = this.total;

            return new BalanceSnapshot(id, status, createdDate, updatedDate,
                    cardId, total, old, dateRange, available);
        }
    }
}
