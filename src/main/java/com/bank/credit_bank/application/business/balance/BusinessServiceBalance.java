package com.bank.credit_bank.application.business.balance;

import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.balance.model.enums.BalanceType;

public interface BusinessServiceBalance {
    Balance get(Long cardId, BalanceType balanceType);

    BalanceId save(Balance balance);
}
