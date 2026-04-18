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
import java.util.Objects;
import java.util.UUID;

import static com.bank.credit_bank.domain.base.constants.DateRangeErrorMessage.DATE_NOT_WITHIN_RANGE;
import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.payment.model.constants.PaymentErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;
import static java.util.Objects.isNull;

public class TotalPayment extends AggregateRoot<PaymentId> implements Payment {
    private final Amount paymentAmount;
    private final Approbation paymentApprobation;
    private final CategoryPaymentEnum category;
    private final CardId cardId;
    private final ChannelPaymentEnum channelPayment;

    public TotalPayment(PaymentId paymentId,
                        StatusEnum status,
                        LocalDateTime createdDate,
                        LocalDateTime updatedDate,
                        Amount paymentAmount,
                        Approbation paymentApprobation,
                        CategoryPaymentEnum category,
                        CardId cardId,
                        ChannelPaymentEnum channelPayment) {

        super(paymentId, status, createdDate, updatedDate);

        isNotConditional(Objects.equals(category, CategoryPaymentEnum.TOTAL),
                new PaymentException(PAYMENT_CATEGORY_NOT_SAME_AS_PAYMENT));

        this.paymentAmount = paymentAmount;
        this.paymentApprobation = paymentApprobation;
        this.category = category;
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
                new BalanceException(DATE_NOT_WITHIN_RANGE));

        isNotConditional(!total.esIgual(available.mas(getPaymentAmount())),
                new PaymentException(TOTAL_PAYMENT_MUST_BE_COMPLETED));
    }

    private void addClosedEvent() {
        addEvent(new PaymentClosedEvent(id.getValue()));
    }

    public static TotalPaymentBuilder builder() {
        return new TotalPaymentBuilder();
    }

    public static class TotalPaymentBuilder {
        private PaymentId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private Amount paymentAmount;
        private Approbation paymentApprobation;
        private CategoryPaymentEnum category;
        private CardId cardId;
        private ChannelPaymentEnum channelPayment;

        public TotalPaymentBuilder id(UUID id) {
            this.id = PaymentId.create(id);
            return this;
        }

        public TotalPaymentBuilder status(StatusEnum status) {
            this.status = status;
            return this;
        }

        public TotalPaymentBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TotalPaymentBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public TotalPaymentBuilder paymentAmount(Amount paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public TotalPaymentBuilder paymentAmount(BigDecimal amount, Integer currency, BigDecimal exchangeRate) {
            isNotNull(amount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(currency, new PaymentException(PAYMENT_CATEGORY_NOT_NULL));
            isNotNull(exchangeRate, new PaymentException(PAYMENT_CATEGORY_NOT_NULL));
            Currency cur = Currency.create(CurrencyEnum.ofValue(currency).orElseThrow(), exchangeRate);
            this.paymentAmount = Amount.create(cur, amount);
            return this;
        }

        public TotalPaymentBuilder category(CategoryPaymentEnum category) {
            this.category = category;
            return this;
        }

        public TotalPaymentBuilder category(Integer category) {
            this.category = CategoryPaymentEnum.ofValue(category).orElseThrow(
                    () -> new PaymentException(PAYMENT_CATEGORY_NOT_NULL));
            return this;
        }

        public TotalPaymentBuilder cardId(CardId cardId) {
            this.cardId = cardId;
            return this;
        }

        public TotalPaymentBuilder cardId(Long cardId) {
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            this.cardId = CardId.create(cardId);
            return this;
        }

        public TotalPaymentBuilder channelPayment(ChannelPaymentEnum channelPayment) {
            this.channelPayment = channelPayment;
            return this;
        }

        public TotalPaymentBuilder channelPayment(Integer channelPayment) {
            this.channelPayment = ChannelPaymentEnum.ofValue(channelPayment).orElseThrow(
                    () -> new PaymentException(CHANGE_PAYMENT_NOT_NULL));
            return this;
        }

        public TotalPaymentBuilder approbation(LocalDateTime date, LocalDateTime approbationDate) {
            this.paymentApprobation = Approbation.create(date, approbationDate);
            return this;
        }

        public TotalPaymentBuilder approbation(LocalDateTime date) {
            this.paymentApprobation = Approbation.create(date);
            return this;
        }

        public TotalPayment build() {
            if (this.id == null) this.id = PaymentId.create(UUID.randomUUID());
            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();
            if (this.paymentApprobation == null) this.paymentApprobation = Approbation.create(LocalDateTime.now());

            isNotNull(paymentAmount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(category, new PaymentException(PAYMENT_CATEGORY_NOT_NULL));
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            isNotNull(channelPayment, new PaymentException(CHANGE_PAYMENT_NOT_NULL));
            isNotConditional(paymentAmount.estaVacio(), new PaymentException(PAYMENT_AMOUNT_NOT_ZERO));

            return new TotalPayment(id, status, createdDate, updatedDate,
                    paymentAmount, paymentApprobation, category, cardId, channelPayment);
        }
    }
}
