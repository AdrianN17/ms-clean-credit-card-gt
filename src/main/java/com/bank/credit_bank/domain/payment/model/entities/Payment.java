package com.bank.credit_bank.domain.payment.model.entities;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Approbation;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.generic.aggregate.AggregateRoot;
import com.bank.credit_bank.domain.payment.events.PaymentClosedEvent;
import com.bank.credit_bank.domain.payment.events.PaymentCreatedEvent;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.domain.payment.model.exceptions.PaymentException;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.payment.model.constants.PaymentErrorMessage.*;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;
import static java.util.Objects.isNull;

public class Payment extends AggregateRoot<PaymentId> {
    private final Amount paymentAmount;
    private final Approbation paymentApprobation;
    private final CategoryPaymentEnum category;
    private final CardId cardId;
    private final ChannelPaymentEnum channelPayment;

    private Payment(PaymentBuilder builder) {
        super(builder.id, builder.status, builder.createdDate, builder.updatedDate);
        this.paymentAmount = builder.paymentAmount;
        this.paymentApprobation = builder.paymentApprobation;
        this.category = builder.category;
        this.cardId = builder.cardId;
        this.channelPayment = builder.channelPayment;
        addCreatedEvent();
    }

    public Amount getPaymentAmount() {
        return paymentAmount;
    }

    public Approbation getPaymentApprobation() {
        return paymentApprobation;
    }

    public CategoryPaymentEnum getCategory() {
        return category;
    }

    public CardId getCardId() {
        return cardId;
    }

    public ChannelPaymentEnum getChannelPayment() {
        return channelPayment;
    }

    public void close() {
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

    private void addClosedEvent() {
        addEvent(new PaymentClosedEvent(id.getValue()));
    }

    public Payment discount(BigDecimal discount) {
        return Payment.builder()
                .paymentAmount(getPaymentAmount().descuento(discount))
                .category(getCategory())
                .cardId(getCardId())
                .channelPayment(getChannelPayment())
                .build();
    }

    public void validateIfPaymentIsInApprobation() {
        isNotConditional(isNull(getPaymentApprobation().getApprobationDate()),
                new PaymentException(PAYMENT_IS_STILL_IN_APPROBATION));
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public static class PaymentBuilder {
        private PaymentId id;
        private StatusEnum status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private Amount paymentAmount;
        private Approbation paymentApprobation;
        private CategoryPaymentEnum category;
        private CardId cardId;
        private ChannelPaymentEnum channelPayment;

        public PaymentBuilder id(UUID id) {
            this.id = PaymentId.create(id);
            return this;
        }

        public PaymentBuilder status(StatusEnum status) {
            this.status = status;
            return this;
        }

        public PaymentBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public PaymentBuilder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public PaymentBuilder paymentAmount(Amount paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public PaymentBuilder approbation(LocalDateTime date, LocalDateTime approbationDate) {
            this.paymentApprobation = Approbation.create(date, approbationDate);
            return this;
        }

        public PaymentBuilder approbation(LocalDateTime date) {
            this.paymentApprobation = Approbation.create(date);
            return this;
        }

        public PaymentBuilder category(CategoryPaymentEnum category) {
            this.category = category;
            return this;
        }

        public PaymentBuilder cardId(CardId cardId) {
            this.cardId = cardId;
            return this;
        }

        public PaymentBuilder channelPayment(ChannelPaymentEnum channelPayment) {
            this.channelPayment = channelPayment;
            return this;
        }

        public Payment build() {
            if (this.id == null)
                this.id = PaymentId.create(UUID.randomUUID());
            if (this.status == null) this.status = ACTIVE;
            if (this.createdDate == null) this.createdDate = LocalDateTime.now();
            if (this.paymentApprobation == null) this.paymentApprobation = Approbation.create(LocalDateTime.now());

            isNotNull(paymentAmount, new PaymentException(PAYMENT_AMOUNT_NOT_NULL));
            isNotNull(category, new PaymentException(PAYMENT_CATEGORY_NOT_NULL));
            isNotNull(cardId, new PaymentException(CARD_ID_NOT_NULL));
            isNotNull(channelPayment, new PaymentException(CHANGE_PAYMENT_NOT_NULL));
            isNotConditional(paymentAmount.estaVacio(), new PaymentException(PAYMENT_AMOUNT_NOT_ZERO));

            return new Payment(this);
        }
    }
}
