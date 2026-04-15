package com.bank.credit_bank.application.consumption.port.in;

import com.bank.credit_bank.application.consumption.queries.FindConsumptionByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;

import java.util.List;

@FunctionalInterface
public interface ConsumptionFindByDatesAndCardIdQuery {
    List<LoadConsumptionView> execute(FindConsumptionByDatesAndCardIdCriteria criteria);
}
