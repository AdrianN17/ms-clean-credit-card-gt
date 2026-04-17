package com.bank.credit_bank.application.businesscurrency;

import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import java.util.UUID;

public interface BusinessServiceConsumption {
    Consumption get(Long cardId, UUID consumptionId);
    ConsumptionId save(Consumption consumption);
}
