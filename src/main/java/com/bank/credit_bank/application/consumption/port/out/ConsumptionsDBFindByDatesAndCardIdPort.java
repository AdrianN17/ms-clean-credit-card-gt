package com.bank.credit_bank.application.consumption.port.out;

import com.bank.credit_bank.application.consumption.queries.FindConsumptionByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;

import java.util.List;

@FunctionalInterface
public interface ConsumptionsDBFindByDatesAndCardIdPort {
    List<LoadConsumptionView> load(FindConsumptionByDatesAndCardIdCriteria criteria);
}
