package com.bank.credit_bank.application.balance.mapper;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.entities.BalanceSnapshot;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;

public class MapperApplicationBalanceImpl implements MapperApplicationBalance {

    @Override
    public Balance toDomain(BalanceResponseDto dto) {
        return BalanceSnapshot.builder()
                .balanceId(dto.id())
                .status(dto.status())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .currency(CurrencyEnum.valueOf(dto.currency()).getValue(), dto.exchangeRate())
                .cardId(dto.cardId())
                .total(dto.total())
                .old(dto.old())
                .available(dto.available())
                .dateRange(dto.startDate(), dto.endDate())
                .build();
    }

    @Override
    public BalanceRequestDto toDto(Balance balance) {
        return new BalanceRequestDto(
                balance.getId().getValue(),
                balance.getStatus().getValue(),
                balance.getCreatedDate(),
                balance.getUpdatedDate(),
                balance.getCardId().getValue(),
                balance.getTotal().getCurrency().getCurrency().getValue(),
                balance.getTotal().getCurrency().getExchangeRate(),
                balance.getTotal().getAmount(),
                balance.getOld().getAmount(),
                balance.getAvailable().getAmount(),
                balance.getDateRange().getStartDate(),
                balance.getDateRange().getEndDate()
        );
    }
}
