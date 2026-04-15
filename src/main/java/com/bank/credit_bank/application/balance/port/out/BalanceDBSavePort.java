package com.bank.credit_bank.application.balance.port.out;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;

import java.util.Optional;

@FunctionalInterface
public interface BalanceDBSavePort {
    Optional<Long> save(BalanceRequestDto balance);
}
