package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query;

import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.ConsumptionEntityMongo;
import org.springframework.stereotype.Component;

@Component
public class ConsumptionQueryMapperMongoImpl implements ConsumptionQueryMapperMongo {

    @Override
    public LoadConsumptionView toView(ConsumptionEntityMongo entity) {
        return new LoadConsumptionView(
                entity.getConsumptionId(),
                entity.getAmount(),
                entity.getCurrency().getCode(),
                entity.getConsumptionDate(),
                entity.getConsumptionApprobationDate(),
                entity.getCardId(),
                entity.getSellerName()
        );
    }
}
