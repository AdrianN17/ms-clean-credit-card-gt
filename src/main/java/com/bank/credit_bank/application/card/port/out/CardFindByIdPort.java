package com.bank.credit_bank.application.card.port.out;

import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;

import java.util.Optional;

@FunctionalInterface
public interface CardFindByIdPort {
    Optional<CardResponseDto> load(Long cardId, CurrencyRequestDto currencyRequestDto);
}
