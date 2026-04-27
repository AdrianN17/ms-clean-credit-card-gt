package com.bank.credit_bank.domain.payment.model.factory;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.payment.model.dto.CreatePaymentRequestDto;
import com.bank.credit_bank.domain.payment.model.dto.CreatePaymentRequestFirstDto;
import com.bank.credit_bank.domain.payment.model.entities.*;

public class PaymentFactoryImpl implements PaymentFactory {

    @Override
    public Payment create(CreatePaymentRequestDto dto) {
        var categoryPayment = CategoryPaymentEnum.ofCode(dto.category()).orElseThrow();

        return switch (categoryPayment) {
            case NORMAL -> buildNormalPayment(dto, categoryPayment);
            case TOTAL -> buildTotalPayment(dto, categoryPayment);
            case MINIMO -> buildMinimunPayment(dto);
            case ADELANTADO -> buildPrepayment(dto);
        };
    }

    @Override
    public Payment create(CreatePaymentRequestFirstDto dto) {
        var categoryPayment = CategoryPaymentEnum.ofCode(dto.category()).orElseThrow();

        return switch (categoryPayment) {
            case NORMAL -> buildNormalPayment(dto, categoryPayment);
            case TOTAL -> buildTotalPayment(dto, categoryPayment);
            case MINIMO -> buildMinimunPayment(dto);
            case ADELANTADO -> buildPrepayment(dto);
        };
    }

    private NormalPayment buildNormalPayment(CreatePaymentRequestDto dto, CategoryPaymentEnum categoryPayment) {
        return NormalPayment.builder()
                .id(dto.id())
                .status(StatusEnum.ofValue(dto.status()).orElseThrow())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .approbation(dto.paymentDate(), dto.paymentApprobationDate())
                .category(categoryPayment)
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private TotalPayment buildTotalPayment(CreatePaymentRequestDto dto, CategoryPaymentEnum categoryPayment) {
        return TotalPayment.builder()
                .id(dto.id())
                .status(StatusEnum.ofValue(dto.status()).orElseThrow())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .approbation(dto.paymentDate(), dto.paymentApprobationDate())
                .category(categoryPayment)
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private MinimunPayment buildMinimunPayment(CreatePaymentRequestDto dto) {
        return MinimunPayment.builder()
                .id(dto.id())
                .status(StatusEnum.ofValue(dto.status()).orElseThrow())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .approbation(dto.paymentDate(), dto.paymentApprobationDate())
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private Prepayment buildPrepayment(CreatePaymentRequestDto dto) {
        return Prepayment.builder()
                .id(dto.id())
                .status(StatusEnum.ofValue(dto.status()).orElseThrow())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .approbation(dto.paymentDate(), dto.paymentApprobationDate())
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private NormalPayment buildNormalPayment(CreatePaymentRequestFirstDto dto, CategoryPaymentEnum categoryPayment) {
        return NormalPayment.builder()
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .category(categoryPayment)
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private TotalPayment buildTotalPayment(CreatePaymentRequestFirstDto dto, CategoryPaymentEnum categoryPayment) {
        return TotalPayment.builder()
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .category(categoryPayment)
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private MinimunPayment buildMinimunPayment(CreatePaymentRequestFirstDto dto) {
        return MinimunPayment.builder()
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }

    private Prepayment buildPrepayment(CreatePaymentRequestFirstDto dto) {
        return Prepayment.builder()
                .paymentAmount(dto.amount(), dto.currency(), dto.exchangeRate())
                .cardId(dto.cardId())
                .channelPayment(dto.channelPayment())
                .build();
    }
}
