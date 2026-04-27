package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.CardEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.CardEntityVO;

public class CardPersistanceMapperImpl implements CardPersistanceMapper {

    @Override
    public CardEntity toEntity(CardRequestDto card) {
        return CardEntity.builder()
                .cardId(card.id())
                .typeCard(TypeCardEnum.ofValue(card.typeCard()).orElseThrow())
                .categoryCard(CategoryCardEnum.ofValue(card.categoryCard()).orElseThrow())
                .createdDate(card.createdDate())
                .updatedDate(card.updatedDate())
                .status(StatusEnum.ofValue(card.status()).orElseThrow())
                .build();
    }

    @Override
    public CardResponseDto toDomain(CardEntityVO cardEntity, CurrencyRequestDto currency) {
        return new CardResponseDto(
                cardEntity.getCardId(),
                cardEntity.getStatus().getValue(),
                cardEntity.getCreatedDate(),
                cardEntity.getUpdatedDate(),
                cardEntity.getTypeCard().name(),
                cardEntity.getCategoryCard().name(),
                cardEntity.getCardAccount().getCurrency().name(),
                currency.exchangeRate(),
                cardEntity.getCardAccount().getCreditTotal(),
                cardEntity.getCardAccount().getDebtTax(),
                cardEntity.getCardAccount().getCardStatus().name(),
                cardEntity.getCardAccount().getCardAccountId(),
                cardEntity.getCardAccount().getPaymentDate()
        );
    }
}
