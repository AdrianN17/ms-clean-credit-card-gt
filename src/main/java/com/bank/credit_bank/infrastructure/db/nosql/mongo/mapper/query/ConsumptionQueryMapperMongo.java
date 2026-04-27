package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query;

import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.ConsumptionEntityMongo;

@FunctionalInterface
public interface ConsumptionQueryMapperMongo {
    LoadConsumptionView toView(ConsumptionEntityMongo entity);
}
