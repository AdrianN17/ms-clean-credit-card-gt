package com.bank.credit_bank.domain.balance.model.entities;

import com.bank.credit_bank.domain.base.vo.Amount;

public interface BalanceOperable extends Balance {

    Boolean isOvercharged();

    void apply(Amount amount);

    void cancel(Amount amount);
}

