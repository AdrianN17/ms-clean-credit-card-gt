package com.bank.credit_bank.application.card.mapper;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.domain.card.model.entities.Card;

public interface MapperApplicationCard {
    Card toDomain(CardResponseDto dto);
    CardRequestDto toDto(Card card);
}
