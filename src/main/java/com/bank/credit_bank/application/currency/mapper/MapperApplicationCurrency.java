package com.bank.credit_bank.application.currency.mapper;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.currency.dto.response.CurrencyResponseDto;

public interface MapperApplicationCurrency {
    CurrencyResponseDto toDtoResponse(CurrencyRequestDto dto);

    CurrencyRequestDto toDtoRequest(CurrencyResponseDto dto);
}
