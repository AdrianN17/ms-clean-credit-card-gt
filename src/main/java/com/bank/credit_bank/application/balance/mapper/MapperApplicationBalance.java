package com.bank.credit_bank.application.balance.mapper;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;

public interface MapperApplicationBalance {
    Balance toDomain(BalanceResponseDto dto);

    BalanceRequestDto toDto(Balance balance);
}
