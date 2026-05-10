package com.bank.credit_bank.infrastructure.config.db.benefit.application;

import com.bank.credit_bank.application.benefit.business.BusinessServiceBenefit;
import com.bank.credit_bank.application.benefit.business.BusinessServiceBenefitImpl;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefitImpl;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBFindByIdPort;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BenefitApplicationConfig {

    @Bean
    public MapperApplicationBenefit mapperApplicationBenefit() {
        return new MapperApplicationBenefitImpl();
    }

    @Bean
    public BusinessServiceBenefit businessServiceBenefit(MapperApplicationBenefit mapperApplicationBenefit,
                                                         BenefitDBFindByIdPort benefitDBFindByIdPort,
                                                         BenefitDBSavePort benefitDBSavePort) {
        return new BusinessServiceBenefitImpl(mapperApplicationBenefit,
                benefitDBFindByIdPort,
                benefitDBSavePort);
    }
}
