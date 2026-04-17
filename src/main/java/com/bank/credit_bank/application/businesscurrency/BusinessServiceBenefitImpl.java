package com.bank.credit_bank.application.businesscurrency;

import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBFindByIdPort;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;

import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.BENEFIT_NOT_FOUND;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.FAILED_TO_CREATE_BENEFIT;

public class BusinessServiceBenefitImpl implements BusinessServiceBenefit {

    private final MapperApplicationBenefit mapperApplicationBenefit;
    private final BenefitDBFindByIdPort benefitDBFindByIdPort;
    private final BenefitDBSavePort benefitDBSavePort;
    private final GenericEventPublisherPort genericEventPublisherPort;

    public BusinessServiceBenefitImpl(MapperApplicationBenefit mapperApplicationBenefit, BenefitDBFindByIdPort benefitDBFindByIdPort, BenefitDBSavePort benefitDBSavePort, GenericEventPublisherPort genericEventPublisherPort) {
        this.mapperApplicationBenefit = mapperApplicationBenefit;
        this.benefitDBFindByIdPort = benefitDBFindByIdPort;
        this.benefitDBSavePort = benefitDBSavePort;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }


    @Override
    public Benefit get(Long cardId) {
        var benefitResponseDto = benefitDBFindByIdPort
                .load(cardId)
                .orElseThrow(() -> new ApplicationBenefitException(BENEFIT_NOT_FOUND));

        return mapperApplicationBenefit.toDomain(benefitResponseDto);
    }

    @Override
    public BenefitId save(Benefit benefit) {
        var benefitRequestDto = mapperApplicationBenefit.toDto(benefit);

        var id = benefitDBSavePort.save(benefitRequestDto)
                .orElseThrow(() -> new ApplicationBenefitException(FAILED_TO_CREATE_BENEFIT));

        benefit.pullDomainEvents().forEach(genericEventPublisherPort::publish);

        return id;
    }
}
