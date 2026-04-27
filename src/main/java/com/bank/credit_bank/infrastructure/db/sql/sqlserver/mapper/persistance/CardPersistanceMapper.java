package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.CardEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.CardEntityVO;

public interface CardPersistanceMapper {
    CardEntity toEntity(CardRequestDto card);

    CardResponseDto toDomain(CardEntityVO cardEntity, CurrencyRequestDto currency);
}
