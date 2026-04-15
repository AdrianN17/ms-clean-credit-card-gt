package com.bank.credit_bank.application.balance.port.out;

import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;

import java.util.Optional;

@FunctionalInterface
public interface BalanceDBFindByIdPort {
    Optional<BalanceResponseDto> load(Long cardId, CurrencyRequestDto currency);
}
