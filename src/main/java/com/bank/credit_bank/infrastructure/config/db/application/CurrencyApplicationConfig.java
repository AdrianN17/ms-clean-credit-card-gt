package com.bank.credit_bank.infrastructure.config.db.application;

import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrencyImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyApplicationConfig {

    @Bean
    public MapperApplicationCurrency mapperApplicationCurrency() {
        return new MapperApplicationCurrencyImpl();
    }
}
