package com.bank.credit_bank.application.card.port.in;

import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;

@FunctionalInterface
public interface CardFindByIdQuery {
    LoadCardBalanceBenefitView execute(Long cardId);
}
