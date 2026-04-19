package com.bank.credit_bank.application.balance.mapper;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.factory.BalanceFactory;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.balance.model.enums.BalanceType;

public class MapperApplicationBalanceImpl implements MapperApplicationBalance {

    private final BalanceFactory balanceFactory;

    public MapperApplicationBalanceImpl(BalanceFactory balanceFactory) {
        this.balanceFactory = balanceFactory;
    }


    @Override
    public Balance toDomain(BalanceResponseDto dto, BalanceType balanceType) {

        return balanceFactory.create(
                dto.id(),
                dto.status(),
                dto.createdDate(),
                dto.updatedDate(),
                CurrencyEnum.valueOf(dto.currency()).getValue(),
                dto.exchangeRate(),
                dto.cardId(),
                dto.total(),
                dto.old(),
                dto.available(),
                dto.startDate(),
                dto.endDate(),
                balanceType
        );
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
