package com.bank.credit_bank.application.benefit.port.out;

import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;

import java.util.Optional;

@FunctionalInterface
public interface BenefitDBFindByIdPort {
    Optional<BenefitResponseDto> load(Long cardId);
}
