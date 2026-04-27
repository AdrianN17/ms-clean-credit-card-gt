package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.CardAccountEntity;

public interface CardAccountPersistanceMapper {
    CardAccountEntity toEntity(CardRequestDto card);
}
