package com.bank.credit_bank.infrastructure.config.db.payment.payment;

import com.bank.credit_bank.application.balance.business.BusinessServiceBalance;
import com.bank.credit_bank.application.benefit.business.BusinessServiceBenefit;
import com.bank.credit_bank.application.card.business.BusinessServiceCard;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import com.bank.credit_bank.application.payment.business.BusinessServicePayment;
import com.bank.credit_bank.application.payment.business.BusinessServicePaymentImpl;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPayment;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPaymentImpl;
import com.bank.credit_bank.application.payment.port.out.PaymentDBFindCurrencyPort;
import com.bank.credit_bank.application.payment.port.out.PaymentDBSavePort;
import com.bank.credit_bank.application.payment.port.out.PaymentFindByIdPort;
import com.bank.credit_bank.application.payment.port.out.PaymentsFindByDatesAndCardIdPort;
import com.bank.credit_bank.application.payment.service.PaymentCancelService;
import com.bank.credit_bank.application.payment.service.PaymentFindByDatesAndCardIdService;
import com.bank.credit_bank.application.payment.service.PaymentProcessService;
import com.bank.credit_bank.domain.payment.model.factory.PaymentFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentApplicationConfig {

    @Bean
    public PaymentProcessService paymentProcessService(BusinessServiceCard businessServiceCard,
                                                       BusinessServiceBalance businessServiceBalance,
                                                       BusinessServiceBenefit businessServiceBenefit,
                                                       BusinessServicePayment businessServicePayment,
                                                       MapperApplicationCurrency mapperApplicationCurrency,
                                                       LoadCurrencyWSPort loadCurrencyWSPort,
                                                       PaymentFactory paymentFactory) {
        return new PaymentProcessService(businessServiceCard, businessServiceBalance, businessServiceBenefit,
                businessServicePayment, mapperApplicationCurrency, loadCurrencyWSPort, paymentFactory);
    }

    @Bean
    public PaymentCancelService paymentCancelService(BusinessServiceCard businessServiceCard,
                                                     BusinessServiceBalance businessServiceBalance,
                                                     BusinessServicePayment businessServicePayment) {
        return new PaymentCancelService(businessServiceCard, businessServiceBalance, businessServicePayment);
    }

    @Bean
    public PaymentFindByDatesAndCardIdService paymentFindByDatesAndCardIdService(
            PaymentsFindByDatesAndCardIdPort paymentsFindByDatesAndCardIdPort) {
        return new PaymentFindByDatesAndCardIdService(paymentsFindByDatesAndCardIdPort);
    }

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
