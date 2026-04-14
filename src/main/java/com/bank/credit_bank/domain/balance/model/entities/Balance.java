package com.bank.credit_bank.domain.balance.model.entities;

import com.bank.credit_bank.domain.balance.events.BalanceClosedEvent;
import com.bank.credit_bank.domain.balance.events.BalanceCreatedEvent;
import com.bank.credit_bank.domain.balance.events.BalanceUpdatedEvent;
import com.bank.credit_bank.domain.balance.model.exceptions.BalanceException;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;
import com.bank.credit_bank.domain.payment.model.entities.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.bank.credit_bank.domain.balance.model.constants.BalanceConstant.OVERCHARGE_LIMIT;
import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.*;
import static com.bank.credit_bank.domain.base.constants.DateRangeErrorMessage.DATE_NOT_WITHIN_RANGE;
import static com.bank.credit_bank.domain.card.model.constants.CardErrorMessage.PAYMENT_CATEGORY_EXCEED_LIKE;
import static com.bank.credit_bank.domain.card.model.constants.CardErrorMessage.PAYMENT_CATEGORY_NOT_SAME_AS_PAYMENT;
import static com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum.ADELANTADO;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class Balance extends AggregateRoot<BalanceId> {
    private final CardId cardId;
    private final Amount total;
    private final Amount old;
    private final DateRange dateRange;
    private Amount available;

    public Balance(BalanceId id,
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
        this.dateRange = dateRange;
        this.available = available;
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

    public Boolean UnavailablePayment(LocalDateTime fecha) {
        return getDateRange().ensureWithinRange(fecha.toLocalDate());
    }

    public Amount calculatePayment(Amount payment) {
        return getAvailable().mas(payment);
    }

    public void applyPayment(Amount payment) {
        this.available = getAvailable().mas(payment);
        addUpdatedEvent();
    }

    public Amount calculateConsumption(Amount consumption) {
        return getAvailable().menos(consumption);
    }

    public void applyConsumption(Amount consumption) {
        this.available = getAvailable().menos(consumption);
        addUpdatedEvent();
    }

    public void applyCancelledPayment(Amount payment) {
        this.available = getAvailable().menos(payment);
        addUpdatedEvent();
    }

    public void applyCancelledConsumption(Amount consumption) {
        this.available = getAvailable().mas(consumption);
        addUpdatedEvent();
    }

    public Boolean isOvercharged() {
        BigDecimal limitOvercharge = getTotal().getAmount().multiply(OVERCHARGE_LIMIT);
        BigDecimal totalLimit = getTotal().getAmount().add(limitOvercharge);

        return getAvailable().getAmount().compareTo(totalLimit) > 0;
    }

    public void prePay(Payment payment) {

        isNotNull(payment, new BalanceException(PAYMENT_CANNOT_BE_NULL));

        isNotConditional(!Objects.equals(payment.getCategory(), ADELANTADO),
                new BalanceException(DATE_NOT_WITHIN_RANGE));

        applyPayment(payment.getPaymentAmount());

        Amount totalAmount = getAvailable();
        Amount totalBalance = getTotal();

        isNotConditional(totalBalance.estaSobrando(totalAmount),
                new BalanceException(PAYMENT_CATEGORY_EXCEED_LIKE + totalBalance.menos(totalAmount).toString()));
    }

    public void pay(Payment payment) {

        isNotNull(payment, new BalanceException(PAYMENT_CANNOT_BE_NULL));

        isNotConditional(UnavailablePayment(payment.getPaymentApprobation().getDate()),
                new BalanceException(DATE_NOT_WITHIN_RANGE));

        Amount totalAmount = getAvailable();
        Amount totalBalance = getTotal();

        isNotConditional(totalBalance.esIgual(totalAmount) && !Objects.equals(payment.getCategory(), CategoryPaymentEnum.TOTAL),
                new BalanceException(PAYMENT_CATEGORY_NOT_SAME_AS_PAYMENT));
        isNotConditional(totalAmount.estaSobrando(totalBalance),
                new BalanceException(PAYMENT_CATEGORY_EXCEED_LIKE + totalAmount.menos(totalBalance).toString()));

        applyPayment(payment.getPaymentAmount());
    }

    public void consumptionSplitted(List<Consumption> consumptions) {
        isNotNull(consumptions, new BalanceException(CONSUMPTIONS_CANNOT_BE_NULL));
        isNotConditional(consumptions.isEmpty(), new BalanceException(CONSUMPTIONS_CANNOT_BE_EMPTY));

        consumptions.forEach(consumption -> {
            isNotNull(consumption, new BalanceException(CONSUMPTION_CANNOT_BE_NULL));
            applyConsumption(consumption.getConsumptionAmount());
        });
    }

    public void cancelConsumption(Consumption consumption) {
        isNotNull(consumption, new BalanceException(CONSUMPTION_CANNOT_BE_NULL));
        applyCancelledConsumption(consumption.getConsumptionAmount());
        consumption.close();
    }

    public void cancelPayment(Payment payment) {
        isNotNull(payment, new BalanceException(PAYMENT_CANNOT_BE_NULL));
        applyCancelledPayment(payment.getPaymentAmount());
        payment.close();
    }

    private void addCreatedEvent() {
        addEvent(
                new BalanceCreatedEvent(
                        id.getValue(),
                        cardId.getValue(),
                        total.getAmount(),
                        old.getAmount(),
                        available.getAmount(),
                        dateRange.getStartDate(),
                        dateRange.getEndDate()
                )
        );
    }

    private void addUpdatedEvent() {
        addEvent(
                new BalanceUpdatedEvent(
                        id.getValue(),
                        cardId.getValue(),
                        available.getAmount()
                )
        );
    }

    private void addClosedEvent() {
        addEvent(
                new BalanceClosedEvent(
                        id.getValue()
                )
        );
    }
}
