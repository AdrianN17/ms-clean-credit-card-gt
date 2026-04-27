package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query;

import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.ConsumptionEntityCosmos;
import org.springframework.stereotype.Component;

@Component
public class ConsumptionQueryMapperCosmosImpl implements ConsumptionQueryMapperCosmos {

    @Override
    public LoadConsumptionView toView(ConsumptionEntityCosmos entity) {
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
