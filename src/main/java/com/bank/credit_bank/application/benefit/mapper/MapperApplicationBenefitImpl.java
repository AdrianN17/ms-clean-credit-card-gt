package com.bank.credit_bank.application.benefit.mapper;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;
import com.bank.credit_bank.domain.benefit.model.vo.DiscountPolicy;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.model.vo.CardId;

public class MapperApplicationBenefitImpl implements MapperApplicationBenefit {

    @Override
    public Benefit toDomain(BenefitResponseDto dto) {
        BenefitId benefitId = BenefitId.create(dto.id());
        StatusEnum status = StatusEnum.ofValue(dto.status()).orElse(StatusEnum.ACTIVE);
        CardId cardId = CardId.create(dto.cardId());
        Point totalPoints = Point.create(dto.pointEarned() != null ? dto.pointEarned() : 0);
        DiscountPolicy discountPolicy = DiscountPolicy.create(dto.hasDiscount(), dto.multiplierPoints());

        return new Benefit(
                benefitId,
                status,
                dto.createdDate(),
                dto.updatedDate(),
                totalPoints,
                discountPolicy,
                cardId
        );
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

