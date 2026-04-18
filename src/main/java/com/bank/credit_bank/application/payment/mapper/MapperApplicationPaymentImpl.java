package com.bank.credit_bank.application.payment.mapper;

import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.payment.model.entities.Payment;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.domain.payment.model.factory.PaymentFactory;

public class MapperApplicationPaymentImpl implements MapperApplicationPayment {

    private final PaymentFactory paymentFactory;

    public MapperApplicationPaymentImpl(PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }


    @Override
    public Payment toDomain(PaymentResponseDto dto) {
        Integer currency = CurrencyEnum.valueOf(dto.currency()).getValue();
        Integer category = CategoryPaymentEnum.valueOf(dto.category()).getValue();
        Integer channelPayment = ChannelPaymentEnum.valueOf(dto.channelPayment()).getValue();

        return paymentFactory.create(
                dto.id(),
                dto.status(),
                dto.createdDate(),
                dto.updatedDate(),
                currency,
                dto.exchangeRate(),
                dto.amount(),
                dto.paymentDate(),
                dto.paymentApprobationDate(),
                category,
                dto.cardId(),
                channelPayment
        );
    }

    @Override
    public PaymentRequestDto toDto(Payment payment) {
        return new PaymentRequestDto(
                payment.getId().getValue(),
                payment.getStatus().getValue(),
                payment.getCreatedDate(),
                payment.getUpdatedDate(),
                payment.getPaymentAmount().getCurrency().getCurrency().getValue(),
                payment.getPaymentAmount().getCurrency().getExchangeRate(),
                payment.getPaymentAmount().getAmount(),
                payment.getPaymentApprobation().getDate(),
                payment.getPaymentApprobation().getApprobationDate(),
                payment.getCategory().getValue(),
                payment.getCardId().getValue(),
                payment.getChannelPayment().getValue()
        );
    }
}
