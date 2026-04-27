package com.bank.credit_bank.domain.balance.model.factory;

import com.bank.credit_bank.domain.balance.model.dto.CreateBalanceRequestDto;
import com.bank.credit_bank.domain.balance.model.dto.CreateBalanceRequestFirstDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.entities.BalanceSnapshot;

public class BalanceFactoryImpl implements BalanceFactory {

    @Override
    public Balance create(CreateBalanceRequestFirstDto dto) {
        return BalanceSnapshot.builder()
                .balanceId(dto.id())
                .currency(dto.currency(), dto.exchangeRate())
                .cardId(dto.cardId())
                .total(dto.total())
                .dateRange(dto.paymentDay())
                .build();
    }

    @Override
    public Balance create(CreateBalanceRequestDto dto) {
        return BalanceSnapshot.builder()
                .balanceId(dto.id())
                .status(dto.status())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .currency(dto.currency(), dto.exchangeRate())
                .cardId(dto.cardId())
                .total(dto.total())
                .old(dto.old())
                .available(dto.available())
                .dateRange(dto.startDate(), dto.endDate())
                .build();
    }
}
