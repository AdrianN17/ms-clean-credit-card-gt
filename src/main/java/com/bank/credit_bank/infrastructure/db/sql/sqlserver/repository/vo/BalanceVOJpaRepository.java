package com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.infrastructure.db.generic.repository.GenericJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.vo.BalanceEntityVO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceVOJpaRepository extends GenericJpaRepository<BalanceEntityVO, Long> {
    @Query("SELECT b FROM BalanceEntityVO b WHERE b.card.cardId = :cardId")
    Optional<BalanceEntityVO> findByCardId(Long cardId);

    default Optional<BalanceEntityVO> findActiveByCardId(Long cardId) {
        return findByCardId(cardId).filter(e -> StatusEnum.ACTIVE.equals(e.getStatus()));
    }
}
