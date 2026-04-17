package com.bank.credit_bank.application.business.benefit;

import com.bank.credit_bank.domain.benefit.model.entities.Benefit;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;

public interface BusinessServiceBenefit {
    Benefit get(Long cardId);

    BenefitId save(Benefit benefit);
}
