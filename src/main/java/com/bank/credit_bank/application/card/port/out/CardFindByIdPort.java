package com.bank.credit_bank.application.card.port.out;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;

import java.util.Optional;

@FunctionalInterface
public interface CardFindByIdPort {
    Optional<CardResponseDto> load(Long cardId, CurrencyRequestDto currencyRequestDto);
}
