package com.bank.credit_bank.application.benefit.mapper;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;

public interface MapperApplicationBenefit {
    Benefit toDomain(BenefitResponseDto dto);

    BenefitRequestDto toDto(Benefit benefit);
}
