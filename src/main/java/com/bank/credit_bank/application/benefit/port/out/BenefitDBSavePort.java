package com.bank.credit_bank.application.benefit.port.out;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;

import java.util.Optional;

@FunctionalInterface
public interface BenefitDBSavePort {
    Optional<Long> save(BenefitRequestDto benefit);
}
