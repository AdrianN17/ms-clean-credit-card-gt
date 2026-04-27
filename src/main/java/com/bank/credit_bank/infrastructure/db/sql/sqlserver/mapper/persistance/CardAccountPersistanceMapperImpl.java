package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.CardAccountEntity;

public class CardAccountPersistanceMapperImpl implements CardAccountPersistanceMapper {

    @Override
    public CardAccountEntity toEntity(CardRequestDto card) {
        return CardAccountEntity.builder()
                .cardAccountId(card.cardAccountId())
                .cardStatus(CardStatusEnum.ofValue(card.cardStatus()).orElseThrow())
                .currency(CurrencyEnum.ofValue(card.currency()).orElseThrow())
                .debtTax(card.debtTax())
                .creditTotal(card.creditTotal())
                .paymentDate(card.paymentDay())
                .createdDate(card.createdDate())
                .updatedDate(card.updatedDate())
                .status(StatusEnum.ofValue(card.status()).orElseThrow())
                .cardId(card.id())
                .build();
    }
}
