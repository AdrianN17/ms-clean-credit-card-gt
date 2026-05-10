package com.bank.credit_bank.infrastructure.config.db.consumption.application;

import com.bank.credit_bank.application.consumption.business.BusinessServiceConsumption;
import com.bank.credit_bank.application.consumption.business.BusinessServiceConsumptionImpl;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumption;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBFindCurrencyPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumptionApplicationConfig {

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
