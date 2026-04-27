package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query;

import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.ConsumptionEntityCosmos;

@FunctionalInterface
public interface ConsumptionQueryMapperCosmos {
    LoadConsumptionView toView(ConsumptionEntityCosmos entity);
}
