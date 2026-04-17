package com.bank.credit_bank.application.payment.mapper;

import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.payment.model.entities.Payment;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;

public class MapperApplicationPaymentImpl implements MapperApplicationPayment {

    @Override
    public Payment toDomain(PaymentResponseDto dto) {
        CurrencyEnum currencyEnum = CurrencyEnum.valueOf(dto.currency());
        Currency currency = Currency.create(currencyEnum, dto.exchangeRate());
        Amount amount = Amount.create(currency, dto.amount());
        CardId cardId = CardId.create(dto.cardId());
        StatusEnum status = StatusEnum.ofValue(dto.status()).orElse(StatusEnum.ACTIVE);
        CategoryPaymentEnum category = CategoryPaymentEnum.valueOf(dto.category());
        ChannelPaymentEnum channelPayment = ChannelPaymentEnum.valueOf(dto.channelPayment());

        return Payment.builder()
                .id(dto.id())
                .status(status)
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .paymentAmount(amount)
                .approbation(dto.paymentDate(), dto.paymentApprobationDate())
                .category(category)
                .cardId(cardId)
                .channelPayment(channelPayment)
                .build();
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
