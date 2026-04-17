package com.bank.credit_bank.application.card.mapper;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;

public class MapperApplicationCardImpl implements MapperApplicationCard {

    @Override
    public Card toDomain(CardResponseDto dto) {
        return Card.builder()
                .cardId(dto.id())
                .typeCard(TypeCardEnum.valueOf(dto.typeCard()).getValue())
                .categoryCard(CategoryCardEnum.valueOf(dto.categoryCard()).getValue())
                .cardAccountId(dto.cardAccountId())
                .paymentDay(dto.paymentDay())
                .credit(dto.creditTotal(), dto.debtTax(), CurrencyEnum.valueOf(dto.currency()).getValue(), dto.exchangeRate())
                .cardStatus(CardStatusEnum.valueOf(dto.cardStatus()).getValue())
                .status(dto.status())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .build();
    }

    @Override
    public CardRequestDto toDto(Card card) {
        return new CardRequestDto(
                card.getId().getValue(),
                card.getStatus().getValue(),
                card.getCreatedDate(),
                card.getUpdatedDate(),
                card.getTypeCard().getValue(),
                card.getCategoryCard().getValue(),
                card.getCredit().getCreditTotal().getCurrency().getCurrency().getValue(),
                card.getCredit().getCreditTotal().getCurrency().getExchangeRate(),
                card.getCredit().getCreditTotal().getAmount(),
                card.getCredit().getDebtTax(),
                card.getCardStatus().getValue(),
                card.getCardAccountId().getValue(),
                card.getPaymentDay().getValue()
        );
    }
}

