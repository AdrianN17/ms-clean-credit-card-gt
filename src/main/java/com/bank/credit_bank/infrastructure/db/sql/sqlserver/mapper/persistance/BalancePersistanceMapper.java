package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BalanceEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.BalanceEntityVO;

public interface BalancePersistanceMapper {
    BalanceResponseDto toDomain(BalanceEntityVO balanceEntity, CurrencyRequestDto currency);

    BalanceEntity toEntity(BalanceRequestDto balance);
}
