package com.bank.credit_bank.infrastructure.config.db.payment.domain;

import com.bank.credit_bank.domain.payment.model.factory.PaymentFactory;
import com.bank.credit_bank.domain.payment.model.factory.PaymentFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentDomainConfig {

    @Bean
    public PaymentFactory paymentFactory() {
        return new PaymentFactoryImpl();
    }
}
