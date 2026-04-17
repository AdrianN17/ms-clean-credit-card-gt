package com.bank.credit_bank.application.balance.port.out;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;

import java.util.Optional;

@FunctionalInterface
public interface BalanceDBSavePort {
    Optional<BalanceId> save(BalanceRequestDto balance);
}
