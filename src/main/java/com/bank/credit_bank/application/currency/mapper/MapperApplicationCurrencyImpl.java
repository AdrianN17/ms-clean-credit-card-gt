package com.bank.credit_bank.application.currency.mapper;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.currency.dto.response.CurrencyResponseDto;

public class MapperApplicationCurrencyImpl implements MapperApplicationCurrency {

    @Override
    public CurrencyResponseDto toDtoResponse(CurrencyRequestDto dto) {
        return new CurrencyResponseDto(
                dto.currency(),
                dto.exchangeRate()
        );
    }

    @Override
    public CurrencyRequestDto toDtoRequest(CurrencyResponseDto dto) {
        return new CurrencyRequestDto(
                dto.currency(),
                dto.exchangeRate()
        );
    }
}

