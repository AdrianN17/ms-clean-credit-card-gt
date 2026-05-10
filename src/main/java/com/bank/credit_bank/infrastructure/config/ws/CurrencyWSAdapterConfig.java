package com.bank.credit_bank.infrastructure.config.ws;

import com.bank.credit_bank.infrastructure.ws.adapter.CurrencyWSAdapter;
import com.bank.credit_bank.infrastructure.ws.mapper.MapperCurrency;
import com.bank.credit_bank.infrastructure.ws.mapper.MapperCurrencyImpl;
import com.bank.credit_bank.infrastructure.ws.repository.CurrencyJsonServerWSRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CurrencyWSAdapterConfig {

    @Bean
    public CurrencyWSAdapter currencyWSAdapter(CurrencyJsonServerWSRepository currencyJsonServerWSRepository,
                                               MapperCurrency mapperCurrency) {
        return new CurrencyWSAdapter(currencyJsonServerWSRepository,
                mapperCurrency);
    }

    @Bean
    public MapperCurrency mapperCurrency() {
        return new MapperCurrencyImpl();
    }

    @Bean
    public CurrencyJsonServerWSRepository currencyJsonServerWSRepository(RestClient restClient) {
        return new CurrencyJsonServerWSRepository(restClient);
    }
}
