package com.bank.credit_bank.application.benefit.port.out;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;

import java.util.Optional;

@FunctionalInterface
public interface BenefitDBSavePort {
    Optional<BenefitId> save(BenefitRequestDto benefit);
}
