package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BenefitEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.BenefitEntityVO;

public class BenefitPersistanceMapperImpl implements BenefitPersistanceMapper {

    @Override
    public BenefitResponseDto toDomain(BenefitEntityVO benefitEntity) {
        return new BenefitResponseDto(
                benefitEntity.getIdBenefit(),
                benefitEntity.getStatus().getValue(),
                benefitEntity.getCreatedDate(),
                benefitEntity.getUpdatedDate(),
                benefitEntity.getTotalPoints(),
                benefitEntity.getHasDiscount(),
                benefitEntity.getMultiplierPoints(),
                benefitEntity.getCard().getCardId()
        );
    }

    @Override
    public BenefitEntity toEntity(BenefitRequestDto benefit) {
        return BenefitEntity.builder()
                .idBenefit(benefit.id())
                .totalPoints(benefit.pointEarned())
                .hasDiscount(benefit.hasDiscount())
                .multiplierPoints(benefit.multiplierPoints())
                .createdDate(benefit.createdDate())
                .updatedDate(benefit.updatedDate())
                .status(StatusEnum.ofValue(benefit.status()).orElseThrow())
                .cardId(benefit.cardId())
                .build();
    }
}
