package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.PaymentEntityCosmos;

public class PaymentPersistanceMapperCosmosImpl implements PaymentPersistanceMapperCosmos {

    @Override
    public PaymentResponseDto toDomain(PaymentEntityCosmos entity, CurrencyRequestDto currency) {
        return new PaymentResponseDto(
                entity.getPaymentId(),
                entity.getStatus().getValue(),
                entity.getCreatedDate(),
                entity.getUpdatedDate(),
                entity.getCurrency().name(),
                currency.exchangeRate(),
                entity.getAmount(),
                entity.getPaymentDate(),
                entity.getPaymentApprobationDate(),
                entity.getCategory().name(),
                Long.parseLong(entity.getCardId()),
                entity.getChannel().name()
        );
    }

    @Override
    public PaymentEntityCosmos toEntity(PaymentRequestDto payment) {
        return PaymentEntityCosmos.builder()
                .paymentId(payment.id())
                .amount(payment.amount())
                .currency(CurrencyEnum.ofValue(payment.currency()).orElseThrow())
                .paymentDate(payment.paymentDate())
                .paymentApprobationDate(payment.paymentApprobationDate())
                .category(CategoryPaymentEnum.ofValue(payment.category()).orElseThrow())
                .cardId(payment.cardId().toString())
                .channel(ChannelPaymentEnum.ofValue(payment.channelPayment()).orElseThrow())
                .createdDate(payment.createdDate())
                .updatedDate(payment.updatedDate())
                .status(StatusEnum.ofValue(payment.status()).orElseThrow())
                .build();
    }
}
