package com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo;

import com.bank.credit_bank.infrastructure.db.generic.repository.GenericJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.CardAccountEntityVO;
import org.springframework.stereotype.Repository;

@Repository
public interface CardAccountVOJpaRepository extends GenericJpaRepository<CardAccountEntityVO, Long> {
}
