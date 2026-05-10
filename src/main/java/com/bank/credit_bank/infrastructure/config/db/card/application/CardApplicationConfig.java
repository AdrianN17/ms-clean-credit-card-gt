package com.bank.credit_bank.infrastructure.config.db.card.application;

import com.bank.credit_bank.application.card.business.BusinessServiceCard;
import com.bank.credit_bank.application.card.business.BusinessServiceCardImpl;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCardImpl;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardApplicationConfig {

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
