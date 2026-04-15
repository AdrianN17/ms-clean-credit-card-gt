package com.bank.credit_bank.application.card.port.out;

import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;

import java.util.Optional;

@FunctionalInterface
public interface CardBalanceBenefitFindByIdPort {
    Optional<LoadCardBalanceBenefitView> loadAll(Long cardId);
}
