package com.bank.credit_bank.domain.payment.model.factory;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.payment.model.entities.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentFactoryImpl implements PaymentFactory {

    @Override
    public Payment create(UUID id,
                          Integer status,
                          LocalDateTime createdDate,
                          LocalDateTime updatedDate,
                          Integer currency,
                          BigDecimal exchangeRate,
                          BigDecimal amount,
                          LocalDateTime paymentDate,
                          LocalDateTime paymentApprobationDate,
                          Integer category,
                          Long cardId,
                          Integer channelPayment) {

        var categoryPayment = CategoryPaymentEnum.ofValue(category).orElseThrow();

        return switch (categoryPayment) {
            case NORMAL -> NormalPayment.builder()
                    .id(id)
                    .status(StatusEnum.ofValue(status).orElseThrow())
                    .createdDate(createdDate)
                    .updatedDate(updatedDate)
                    .paymentAmount(amount, currency, exchangeRate)
                    .approbation(paymentDate, paymentApprobationDate)
                    .category(categoryPayment)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
            case TOTAL -> TotalPayment.builder()
                    .id(id)
                    .status(StatusEnum.ofValue(status).orElseThrow())
                    .createdDate(createdDate)
                    .updatedDate(updatedDate)
                    .paymentAmount(amount, currency, exchangeRate)
                    .approbation(paymentDate, paymentApprobationDate)
                    .category(categoryPayment)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
            case MINIMO -> MinimunPayment.builder()
                    .id(id)
                    .status(StatusEnum.ofValue(status).orElseThrow())
                    .createdDate(createdDate)
                    .updatedDate(updatedDate)
                    .paymentAmount(amount, currency, exchangeRate)
                    .approbation(paymentDate, paymentApprobationDate)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
            case ADELANTADO -> Prepayment.builder()
                    .id(id)
                    .status(StatusEnum.ofValue(status).orElseThrow())
                    .createdDate(createdDate)
                    .updatedDate(updatedDate)
                    .paymentAmount(amount, currency, exchangeRate)
                    .approbation(paymentDate, paymentApprobationDate)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
        };
    }

    @Override
    public Payment create(Integer currency, BigDecimal exchangeRate, BigDecimal amount, Integer category, Long cardId, Integer channelPayment) {
        var categoryPayment = CategoryPaymentEnum.ofValue(category).orElseThrow();

        return switch (categoryPayment) {
            case NORMAL -> NormalPayment.builder()
                    .paymentAmount(amount, currency, exchangeRate)
                    .category(categoryPayment)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
            case TOTAL -> TotalPayment.builder()
                    .paymentAmount(amount, currency, exchangeRate)
                    .category(categoryPayment)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
            case MINIMO -> MinimunPayment.builder()
                    .paymentAmount(amount, currency, exchangeRate)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
            case ADELANTADO -> Prepayment.builder()
                    .paymentAmount(amount, currency, exchangeRate)
                    .cardId(cardId)
                    .channelPayment(channelPayment)
                    .build();
        };
    }
}


