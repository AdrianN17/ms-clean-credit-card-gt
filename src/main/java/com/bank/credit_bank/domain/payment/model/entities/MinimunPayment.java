package com.bank.credit_bank.domain.payment.model.entities;

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
import static com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum.MINIMO;
import static com.bank.credit_bank.domain.payment.model.constants.PaymentErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;
import static java.util.Objects.isNull;

public class MinimunPayment extends AggregateRoot<PaymentId> implements Payment {
    private final Amount paymentAmount;
    private final Approbation paymentApprobation;
    private final CategoryPaymentEnum category;
    private final CardId cardId;
    private final ChannelPaymentEnum channelPayment;

    public MinimunPayment(PaymentId paymentId,
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
        this.category = MINIMO;
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

        isNotConditional(!dateRange.ensureWithinRange(paymentApprobation.getDate().toLocalDate()),
                new PaymentException(DATE_NOT_WITHIN_RANGE));

        isNotConditional(total.esIgual(available),
                new PaymentException(PAYMENT_IT_NOT_NECCESARY));

        var totalAvailableFraction = total.menos(available).dividir(3);

        isNotConditional(totalAvailableFraction.estaSobrando(getPaymentAmount()),
                new PaymentException(MINIMUN_PAYMENT_MUST_BE_THIRD_PART_OF_AVAILABLE));
    }

    private void addClosedEvent() {
        addEvent(new PaymentClosedEvent(id.getValue()));
    }

    public static MinimunPaymentBuilder builder() {
        return new MinimunPaymentBuilder();
    }

    public static class MinimunPaymentBuilder {
        private PaymentId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private Amount paymentAmount;
        private Approbation paymentApprobation;
        private CardId cardId;
        private ChannelPaymentEnum channelPayment;

        public MinimunPaymentBuilder id(UUID id) {
            this.id = PaymentId.create(id);
            return this;
        }

        public MinimunPaymentBuilder status(StatusEnum status) {
            this.status = status;
            return this;
        }

        public MinimunPaymentBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public MinimunPaymentBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public MinimunPaymentBuilder paymentAmount(Amount paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public MinimunPaymentBuilder paymentAmount(BigDecimal amount, Integer currency, BigDecimal exchangeRate) {
            isNotNull(amount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(currency, new PaymentException(PAYMENT_CURRENCY_NOT_NULL));
            isNotNull(exchangeRate, new PaymentException(PAYMENT_EXCHANGE_RATE_NOT_NULL));
            Currency cur = Currency.create(CurrencyEnum.ofValue(currency).orElseThrow(), exchangeRate);
            this.paymentAmount = Amount.create(cur, amount);
            return this;
        }

        public MinimunPaymentBuilder cardId(CardId cardId) {
            this.cardId = cardId;
            return this;
        }

        public MinimunPaymentBuilder cardId(Long cardId) {
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            this.cardId = CardId.create(cardId);
            return this;
        }

        public MinimunPaymentBuilder channelPayment(ChannelPaymentEnum channelPayment) {
            this.channelPayment = channelPayment;
            return this;
        }

        public MinimunPaymentBuilder channelPayment(Integer channelPayment) {
            this.channelPayment = ChannelPaymentEnum.ofValue(channelPayment).orElseThrow(
                    () -> new PaymentException(CHANNEL_PAYMENT_NOT_NULL));
            return this;
        }

        public MinimunPaymentBuilder approbation(LocalDateTime date, LocalDateTime approbationDate) {
            this.paymentApprobation = Approbation.create(date, approbationDate);
            return this;
        }

        public MinimunPaymentBuilder approbation(LocalDateTime date) {
            this.paymentApprobation = Approbation.create(date);
            return this;
        }

        public MinimunPayment build() {
            if (this.id == null) this.id = PaymentId.create(UUID.randomUUID());
            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();
            if (this.paymentApprobation == null) this.paymentApprobation = Approbation.create(LocalDateTime.now());

            isNotNull(paymentAmount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            isNotNull(channelPayment, new PaymentException(CHANNEL_PAYMENT_NOT_NULL));
            isNotConditional(paymentAmount.estaVacio(), new PaymentException(PAYMENT_AMOUNT_NOT_ZERO));

            return new MinimunPayment(id, status, createdDate, updatedDate,
                    paymentAmount, paymentApprobation, cardId, channelPayment);
        }
    }
}
