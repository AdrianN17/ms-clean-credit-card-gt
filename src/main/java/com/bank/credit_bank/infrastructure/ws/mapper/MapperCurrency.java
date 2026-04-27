package com.bank.credit_bank.infrastructure.ws.mapper;

import com.bank.credit_bank.application.currency.dto.response.CurrencyResponseDto;
import com.bank.credit_bank.infrastructure.ws.dto.CurrencyDto;

@FunctionalInterface
public interface MapperCurrency {
    CurrencyResponseDto toDomain(CurrencyDto currencyDto);
}
