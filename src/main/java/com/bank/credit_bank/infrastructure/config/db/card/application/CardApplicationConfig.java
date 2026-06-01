package com.bank.credit_bank.infrastructure.config.db.card.application;

import com.bank.credit_bank.application.balance.business.BusinessServiceBalance;
import com.bank.credit_bank.application.benefit.business.BusinessServiceBenefit;
import com.bank.credit_bank.application.card.business.BusinessServiceCard;
import com.bank.credit_bank.application.card.business.BusinessServiceCardImpl;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCardImpl;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.card.port.out.CardBalanceBenefitFindByIdPort;
import com.bank.credit_bank.application.card.service.CardCloseService;
import com.bank.credit_bank.application.card.service.CardCreateService;
import com.bank.credit_bank.application.card.service.CardFindByIdService;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import com.bank.credit_bank.application.generator.port.out.IdGeneratePort;
import com.bank.credit_bank.domain.balance.model.factory.BalanceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardApplicationConfig {

    @Bean
    public CardCloseService cardCloseService(BusinessServiceCard businessServiceCard,
                                             BusinessServiceBalance businessServiceBalance,
                                             BusinessServiceBenefit businessServiceBenefit) {
        return new CardCloseService(businessServiceCard, businessServiceBalance, businessServiceBenefit);
    }

    @Bean
    public CardFindByIdService cardFindByIdService(CardBalanceBenefitFindByIdPort cardBalanceBenefitFindByIdPort) {
        return new CardFindByIdService(cardBalanceBenefitFindByIdPort);
    }

    @Bean
    public CardCreateService cardCreateService(BusinessServiceCard businessServiceCard,
                                               BusinessServiceBalance businessServiceBalance,
                                               BusinessServiceBenefit businessServiceBenefit,
                                               IdGeneratePort idGeneratePort,
                                               LoadCurrencyWSPort loadCurrencyWSPort,
                                               BalanceFactory balanceFactory) {
        return new CardCreateService(businessServiceCard, businessServiceBalance, businessServiceBenefit,
                idGeneratePort, loadCurrencyWSPort, balanceFactory);
    }

    @Bean
    public BusinessServiceCard businessServiceCard(CardFindByIdPort cardFindByIdPort,
                                            CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                            MapperApplicationCard mapperApplicationCard,
                                            MapperApplicationCurrency mapperApplicationCurrency,
                                            LoadCurrencyWSPort loadCurrencyWSPort,
                                            CardDBSavePort cardDBSavePort)
    {
        return new BusinessServiceCardImpl(cardFindByIdPort,
                cardDBFindCurrencyPort,
                mapperApplicationCard,
                mapperApplicationCurrency,
                loadCurrencyWSPort,
                cardDBSavePort);
    }

    @Bean
    public MapperApplicationCard mapperApplicationCard() {
        return new MapperApplicationCardImpl();
    }
}
