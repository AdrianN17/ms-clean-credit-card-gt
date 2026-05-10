package com.bank.credit_bank.infrastructure.config.db.balance.application;

import com.bank.credit_bank.application.balance.business.BusinessServiceBalance;
import com.bank.credit_bank.application.balance.business.BusinessServiceBalanceImpl;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalanceImpl;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BalanceApplicationConfig {

    @Bean
    public BusinessServiceBalance businessServiceBalance(BalanceDBFindByIdPort balanceDBFindByIdPort,
                                                         CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                                         MapperApplicationCurrency mapperApplicationCurrency,
                                                         LoadCurrencyWSPort loadCurrencyWSPort,
                                                         MapperApplicationBalance mapperApplicationBalance,
                                                         BalanceDBSavePort balanceDBSavePort) {
        return new BusinessServiceBalanceImpl(balanceDBFindByIdPort,
                cardDBFindCurrencyPort,
                mapperApplicationCurrency,
                loadCurrencyWSPort,
                mapperApplicationBalance,
                balanceDBSavePort);
    }

    @Bean
    public MapperApplicationBalance mapperApplicationBalance() {
        return new MapperApplicationBalanceImpl();
    }
}
