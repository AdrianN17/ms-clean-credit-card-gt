package com.bank.credit_bank.infrastructure.ws.mapper;

import com.bank.credit_bank.application.currency.dto.response.CurrencyResponseDto;
import com.bank.credit_bank.infrastructure.ws.dto.CurrencyDto;

public class MapperCurrencyImpl implements MapperCurrency {

    @Override
    public CurrencyResponseDto toDomain(CurrencyDto currencyDto) {
        return new CurrencyResponseDto(
                currencyDto.currency(),
                currencyDto.value()
        );
    }

}
