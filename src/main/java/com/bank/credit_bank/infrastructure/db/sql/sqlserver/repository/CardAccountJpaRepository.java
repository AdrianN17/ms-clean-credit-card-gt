package com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository;

import com.bank.credit_bank.infrastructure.db.generic.repository.GenericJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.CardAccountEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CardAccountJpaRepository extends GenericJpaRepository<CardAccountEntity, Long> {
}
