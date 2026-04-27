package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BalanceEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.BalanceEntityVO;

public class BalancePersistanceMapperImpl implements BalancePersistanceMapper {

    @Override
    public BalanceResponseDto toDomain(BalanceEntityVO balanceEntity, CurrencyRequestDto currency) {
        return new BalanceResponseDto(
                balanceEntity.getIdBalance(),
                balanceEntity.getStatus().getValue(),
                balanceEntity.getCreatedDate(),
                balanceEntity.getUpdatedDate(),
                balanceEntity.getCard().getCardId(),
                balanceEntity.getCurrency().name(),
                currency.exchangeRate(),
                balanceEntity.getTotalAmount(),
                balanceEntity.getOldAmount(),
                balanceEntity.getAvailableAmount(),
                balanceEntity.getStartDate(),
                balanceEntity.getEndDate()
        );
    }

    @Override
    public BalanceEntity toEntity(BalanceRequestDto balance) {
        return BalanceEntity.builder()
                .idBalance(balance.id())
                .totalAmount(balance.total())
                .oldAmount(balance.old())
                .availableAmount(balance.available())
                .startDate(balance.startDate())
                .endDate(balance.endDate())
                .createdDate(balance.createdDate())
                .updatedDate(balance.updatedDate())
                .status(StatusEnum.ofValue(balance.status()).orElseThrow())
                .currency(CurrencyEnum.ofValue(balance.currency()).orElseThrow())
                .cardId(balance.cardId())
                .build();
    }
}
