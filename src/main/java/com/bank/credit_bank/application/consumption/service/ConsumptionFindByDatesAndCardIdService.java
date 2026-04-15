package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.consumption.port.in.ConsumptionFindByDatesAndCardIdQuery;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionsDBFindByDatesAndCardIdPort;
import com.bank.credit_bank.application.consumption.queries.FindConsumptionByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;

import java.util.List;

public class ConsumptionFindByDatesAndCardIdService implements ConsumptionFindByDatesAndCardIdQuery {

    private final ConsumptionsDBFindByDatesAndCardIdPort consumptionsDBFindByDatesAndCardIdPort;

    public ConsumptionFindByDatesAndCardIdService(ConsumptionsDBFindByDatesAndCardIdPort consumptionsDBFindByDatesAndCardIdPort) {
        this.consumptionsDBFindByDatesAndCardIdPort = consumptionsDBFindByDatesAndCardIdPort;
    }

    @Override
    public List<LoadConsumptionView> execute(FindConsumptionByDatesAndCardIdCriteria criteria) {
        return consumptionsDBFindByDatesAndCardIdPort.load(criteria);
    }
}
