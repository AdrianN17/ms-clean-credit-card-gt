package com.bank.credit_bank.domain.balance.model.factory;

import com.bank.credit_bank.domain.balance.model.dto.CreateBalanceRequestDto;
import com.bank.credit_bank.domain.balance.model.dto.CreateBalanceRequestFirstDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;

public interface BalanceFactory {

    Balance create(
            CreateBalanceRequestFirstDto dto
    );

    Balance create(
            CreateBalanceRequestDto dto
    );
}
