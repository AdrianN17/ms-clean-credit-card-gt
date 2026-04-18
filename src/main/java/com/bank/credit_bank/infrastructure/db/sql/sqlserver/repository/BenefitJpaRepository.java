package com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository;

import com.bank.credit_bank.infrastructure.db.generic.repository.GenericJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BenefitEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitJpaRepository extends GenericJpaRepository<BenefitEntity, Long> {
}
