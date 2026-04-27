package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BenefitEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.BenefitEntityVO;

public interface BenefitPersistanceMapper {
    BenefitResponseDto toDomain(BenefitEntityVO benefitEntity);

    BenefitEntity toEntity(BenefitRequestDto benefit);
}
