package com.bank.credit_bank.infrastructure.config.db.balance.domain;

import com.bank.credit_bank.domain.balance.model.factory.BalanceFactory;
import com.bank.credit_bank.domain.balance.model.factory.BalanceFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BalanceDomainConfig {

    @Bean
    public BalanceFactory balanceFactory(){
        return new BalanceFactoryImpl();
    }
}
