package com.bank.credit_bank.application.balance.business;

import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;

public interface BusinessServiceBalance {
    Balance get(Long cardId);
    BalanceId save(Balance balance);
}
