package com.bank.credit_bank.infrastructure.config.db.consumption.application;

import com.bank.credit_bank.application.balance.business.BusinessServiceBalance;
import com.bank.credit_bank.application.benefit.business.BusinessServiceBenefit;
import com.bank.credit_bank.application.card.business.BusinessServiceCard;
import com.bank.credit_bank.application.consumption.business.BusinessServiceConsumption;
import com.bank.credit_bank.application.consumption.business.BusinessServiceConsumptionImpl;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumption;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumptionImpl;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBFindCurrencyPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionFindByIdPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionsDBFindByDatesAndCardIdPort;
import com.bank.credit_bank.application.consumption.service.ConsumptionCancelService;
import com.bank.credit_bank.application.consumption.service.ConsumptionFindByDatesAndCardIdService;
import com.bank.credit_bank.application.consumption.service.ConsumptionProcessService;
import com.bank.credit_bank.application.consumption.service.ConsumptionSplitService;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumptionApplicationConfig {

    @Bean
    public ConsumptionProcessService consumptionProcessService(BusinessServiceCard businessServiceCard,
                                                               BusinessServiceBalance businessServiceBalance,
                                                               BusinessServiceBenefit businessServiceBenefit,
                                                               BusinessServiceConsumption businessServiceConsumption,
                                                               MapperApplicationCurrency mapperApplicationCurrency,
                                                               LoadCurrencyWSPort loadCurrencyWSPort) {
        return new ConsumptionProcessService(businessServiceCard, businessServiceBalance, businessServiceBenefit,
                businessServiceConsumption, mapperApplicationCurrency, loadCurrencyWSPort);
    }

    @Bean
    public ConsumptionCancelService consumptionCancelService(BusinessServiceCard businessServiceCard,
                                                             BusinessServiceBalance businessServiceBalance,
                                                             BusinessServiceConsumption businessServiceConsumption) {
        return new ConsumptionCancelService(businessServiceCard, businessServiceBalance, businessServiceConsumption);
    }

    @Bean
    public ConsumptionSplitService consumptionSplitService(BusinessServiceCard businessServiceCard,
                                                           BusinessServiceBalance businessServiceBalance,
                                                           BusinessServiceConsumption businessServiceConsumption) {
        return new ConsumptionSplitService(businessServiceCard, businessServiceBalance, businessServiceConsumption);
    }

    @Bean
    public ConsumptionFindByDatesAndCardIdService consumptionFindByDatesAndCardIdService(
            ConsumptionsDBFindByDatesAndCardIdPort consumptionsDBFindByDatesAndCardIdPort) {
        return new ConsumptionFindByDatesAndCardIdService(consumptionsDBFindByDatesAndCardIdPort);
    }

    @Bean
    public MapperApplicationConsumption mapperApplicationConsumption() {
        return new MapperApplicationConsumptionImpl();
    }

    @Bean
    public BusinessServiceConsumption businessServiceConsumption(ConsumptionFindByIdPort consumptionFindByIdPort,
                                                                 ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort,
                                                                 LoadCurrencyWSPort loadCurrencyWSPort,
                                                                 MapperApplicationCurrency mapperApplicationCurrency,
                                                                 MapperApplicationConsumption mapperApplicationConsumption,
                                                                 ConsumptionDBSavePort consumptionDBSavePort) {
        return new BusinessServiceConsumptionImpl(consumptionFindByIdPort,
                consumptionDBFindCurrencyPort,
                loadCurrencyWSPort,
                mapperApplicationCurrency,
                mapperApplicationConsumption,
                consumptionDBSavePort);
    }
}
