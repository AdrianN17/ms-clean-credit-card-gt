package com.bank.credit_bank.application.benefit.mapper;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;

public class MapperApplicationBenefitImpl implements MapperApplicationBenefit {

    @Override
    public Benefit toDomain(BenefitResponseDto dto) {
        return Benefit.builder()
                .benefitId(dto.id())
                .cardId(dto.cardId())
                .totalPoints(dto.pointEarned())
                .discountPolicy(dto.hasDiscount(), dto.multiplierPoints())
                .status(dto.status())
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .build();
    }

    @Override
    public BenefitRequestDto toDto(Benefit benefit) {
        return new BenefitRequestDto(
                benefit.getId().getValue(),
                benefit.getStatus().getValue(),
                benefit.getCreatedDate(),
                benefit.getUpdatedDate(),
                benefit.getTotalPoints().getPointEarned(),
                benefit.getDiscountPolicy().getHasDiscount(),
                benefit.getDiscountPolicy().getMultiplierPoints(),
                benefit.getCardId().getValue()
        );
    }
}
