package com.bank.credit_bank.infrastructure.config.db.payment.payment;

import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import com.bank.credit_bank.application.payment.business.BusinessServicePayment;
import com.bank.credit_bank.application.payment.business.BusinessServicePaymentImpl;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPayment;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPaymentImpl;
import com.bank.credit_bank.application.payment.port.out.PaymentDBFindCurrencyPort;
import com.bank.credit_bank.application.payment.port.out.PaymentDBSavePort;
import com.bank.credit_bank.application.payment.port.out.PaymentFindByIdPort;
import com.bank.credit_bank.domain.payment.model.factory.PaymentFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentApplicationConfig {

    @Bean
    public BusinessServicePayment businessServicePayment(PaymentFindByIdPort paymentFindByIdPort,
                                                         LoadCurrencyWSPort loadCurrencyWSPort,
                                                         PaymentDBFindCurrencyPort loadConsumptionCurrencyPort,
                                                         MapperApplicationCurrency mapperApplicationCurrency,
                                                         MapperApplicationPayment mapperApplicationPayment,
                                                         PaymentDBSavePort paymentDBSavePort) {
        return new BusinessServicePaymentImpl(paymentFindByIdPort,
                loadCurrencyWSPort,
                loadConsumptionCurrencyPort,
                mapperApplicationCurrency,
                mapperApplicationPayment,
                paymentDBSavePort);
    }

    @Bean
    public MapperApplicationPayment mapperApplicationPayment(PaymentFactory paymentFactory) {
        return new MapperApplicationPaymentImpl(paymentFactory);
    }
}
