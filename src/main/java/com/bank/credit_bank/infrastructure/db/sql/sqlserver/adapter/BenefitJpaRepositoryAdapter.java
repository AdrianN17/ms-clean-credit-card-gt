package com.bank.credit_bank.infrastructure.db.sql.sqlserver.adapter;

import com.bank.credit_bank.application.benefit.dto.request.BenefitRequestDto;
import com.bank.credit_bank.application.benefit.dto.response.BenefitResponseDto;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBFindByIdPort;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.domain.benefit.model.vo.BenefitId;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BenefitEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.BenefitPersistanceException;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.BenefitPersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.BenefitJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo.BenefitVOJpaRepository;

import java.util.Optional;

import static com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.BenefitErrorMessage.BENEFIT_NOT_SAVED;

public class BenefitJpaRepositoryAdapter implements BenefitDBSavePort, BenefitDBFindByIdPort {

    private final BenefitJpaRepository benefitJpaRepository;
    private final BenefitVOJpaRepository benefitVOJpaRepository;
    private final BenefitPersistanceMapper benefitPersistenceMapper;

    public BenefitJpaRepositoryAdapter(BenefitJpaRepository benefitJpaRepository, BenefitVOJpaRepository benefitVOJpaRepository, BenefitPersistanceMapper benefitPersistenceMapper) {
        this.benefitJpaRepository = benefitJpaRepository;
        this.benefitVOJpaRepository = benefitVOJpaRepository;
        this.benefitPersistenceMapper = benefitPersistenceMapper;
    }

    @Override
    public Optional<BenefitId> save(BenefitRequestDto benefit) {
        Optional<BenefitEntity> benefitEntity = Optional.of(benefitJpaRepository.save(benefitPersistenceMapper.toEntity(benefit)));
        return Optional.ofNullable(benefitEntity
                        .orElseThrow(() -> new BenefitPersistanceException(BENEFIT_NOT_SAVED)))
                .map(BenefitEntity::getIdBenefit).map(BenefitId::create);
    }

    @Override
    public Optional<BenefitResponseDto> load(Long cardId) {
        return benefitVOJpaRepository.findActiveByCardId(cardId)
                .map(benefitPersistenceMapper::toDomain);
    }

}
