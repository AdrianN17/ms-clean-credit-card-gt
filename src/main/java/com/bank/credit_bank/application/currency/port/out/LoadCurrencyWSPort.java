package com.bank.credit_bank.application.currency.port.out;

import com.bank.credit_bank.application.currency.dto.response.CurrencyResponseDto;

import java.util.Optional;

@FunctionalInterface
public interface LoadCurrencyWSPort {
    Optional<CurrencyResponseDto> load(String currencyEnum);
}
