package com.bank.credit_bank.domain.payment.model.entities;

import com.bank.credit_bank.domain.balance.model.exceptions.BalanceException;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Approbation;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;
import com.bank.credit_bank.domain.payment.events.PaymentClosedEvent;
import com.bank.credit_bank.domain.payment.events.PaymentCreatedEvent;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.domain.payment.model.exceptions.PaymentException;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.bank.credit_bank.domain.base.constants.DateRangeErrorMessage.DATE_NOT_WITHIN_RANGE;
import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum.ADELANTADO;
import static com.bank.credit_bank.domain.payment.model.constants.PaymentErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;
import static java.util.Objects.isNull;

public class Prepayment extends AggregateRoot<PaymentId> implements Payment {
    private final Amount paymentAmount;
    private final Approbation paymentApprobation;
    private final CategoryPaymentEnum category;
    private final CardId cardId;
    private final ChannelPaymentEnum channelPayment;

    public Prepayment(PaymentId paymentId,
                      StatusEnum status,
                      LocalDateTime createdDate,
                      LocalDateTime updatedDate,
                      Amount paymentAmount,
                      Approbation paymentApprobation,
                      CardId cardId,
                      ChannelPaymentEnum channelPayment) {

        super(paymentId, status, createdDate, updatedDate);

        this.paymentAmount = paymentAmount;
        this.paymentApprobation = paymentApprobation;
        this.category = ADELANTADO;
        this.cardId = cardId;
        this.channelPayment = channelPayment;

        addCreatedEvent();
    }

    @Override
    public Amount getPaymentAmount() {
        return this.paymentAmount;
    }

    @Override
    public Approbation getPaymentApprobation() {
        return this.paymentApprobation;
    }

    @Override
    public CategoryPaymentEnum getCategory() {
        return this.category;
    }

    @Override
    public CardId getCardId() {
        return this.cardId;
    }

    @Override
    public ChannelPaymentEnum getChannelPayment() {
        return this.channelPayment;
    }

    @Override
    public void close() {
        isNotConditional(isNull(getPaymentApprobation().getApprobationDate()),
                new PaymentException(PAYMENT_IS_STILL_IN_APPROBATION));

        softDelete();
        addClosedEvent();
    }

    private void addCreatedEvent() {
        addEvent(new PaymentCreatedEvent(
                id.getValue(),
                cardId.getValue(),
                paymentAmount.getAmount(),
                paymentAmount.getCurrency().getCurrency().getValue(),
                paymentAmount.getCurrency().getExchangeRate(),
                category.getValue(),
                channelPayment.getValue(),
                paymentApprobation.getDate()
        ));
    }

    @Override
    public void validateIfPaymentIsPossible(Amount available, Amount total, DateRange dateRange) {

        isNotConditional(dateRange.ensureWithinRange(paymentApprobation.getDate().toLocalDate()),
                new PaymentException(DATE_NOT_WITHIN_RANGE));

        isNotConditional(total.esIgual(getPaymentAmount()),
                new PaymentException(PAYMENT_IT_NOT_NECCESARY));
    }

    private void addClosedEvent() {
        addEvent(new PaymentClosedEvent(id.getValue()));
    }

    public static PrepaymentBuilder builder() {
        return new PrepaymentBuilder();
    }

    public static class PrepaymentBuilder {
        private PaymentId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private Amount paymentAmount;
        private Approbation paymentApprobation;
        private CardId cardId;
        private ChannelPaymentEnum channelPayment;

        public PrepaymentBuilder id(UUID id) {
            this.id = PaymentId.create(id);
            return this;
        }

        public PrepaymentBuilder status(StatusEnum status) {
            this.status = status;
            return this;
        }

        public PrepaymentBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public PrepaymentBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public PrepaymentBuilder paymentAmount(Amount paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public PrepaymentBuilder paymentAmount(BigDecimal amount, String currency, BigDecimal exchangeRate) {
            isNotNull(amount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(currency, new PaymentException(PAYMENT_CURRENCY_NOT_NULL));
            isNotNull(exchangeRate, new PaymentException(PAYMENT_EXCHANGE_RATE_NOT_NULL));
            Currency cur = Currency.create(CurrencyEnum.ofCode(currency).orElseThrow(), exchangeRate);
            this.paymentAmount = Amount.create(cur, amount);
            return this;
        }

        public PrepaymentBuilder cardId(CardId cardId) {
            this.cardId = cardId;
            return this;
        }

        public PrepaymentBuilder cardId(Long cardId) {
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            this.cardId = CardId.create(cardId);
            return this;
        }

        public PrepaymentBuilder channelPayment(ChannelPaymentEnum channelPayment) {
            this.channelPayment = channelPayment;
            return this;
        }

        public PrepaymentBuilder channelPayment(String channelPayment) {
            this.channelPayment = ChannelPaymentEnum.ofCode(channelPayment).orElseThrow(
                    () -> new PaymentException(CHANNEL_PAYMENT_NOT_NULL));
            return this;
        }

        public PrepaymentBuilder approbation(LocalDateTime date, LocalDateTime approbationDate) {
            this.paymentApprobation = Approbation.create(date, approbationDate);
            return this;
        }

        public PrepaymentBuilder approbation(LocalDateTime date) {
            this.paymentApprobation = Approbation.create(date);
            return this;
        }

        public Prepayment build() {
            if (this.id == null) this.id = PaymentId.create(UUID.randomUUID());
            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();
            if (this.paymentApprobation == null) this.paymentApprobation = Approbation.create(LocalDateTime.now());

            isNotNull(paymentAmount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            isNotNull(channelPayment, new PaymentException(CHANNEL_PAYMENT_NOT_NULL));
            isNotConditional(paymentAmount.estaVacio(), new PaymentException(PAYMENT_AMOUNT_NOT_ZERO));

            return new Prepayment(id, status, createdDate, updatedDate,
                    paymentAmount, paymentApprobation, cardId, channelPayment);
        }
    }
}
