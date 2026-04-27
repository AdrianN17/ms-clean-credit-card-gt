package com.bank.credit_bank.infrastructure.db.sql.sqlserver.adapter;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.BalanceEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.BalancePersistanceException;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.BalancePersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.BalanceJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo.BalanceVOJpaRepository;

import java.util.Optional;

import static com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.BalanceErrorMessage.BALANCE_NOT_SAVED;

public class BalanceJpaRepositoryAdapter implements BalanceDBSavePort, BalanceDBFindByIdPort {

    private final BalanceJpaRepository balanceJpaRepository;
    private final BalanceVOJpaRepository balanceVOJpaRepository;
    private final BalancePersistanceMapper balancePersistenceMapper;

    public BalanceJpaRepositoryAdapter(BalanceJpaRepository balanceJpaRepository, BalanceVOJpaRepository balanceVOJpaRepository, BalancePersistanceMapper balancePersistenceMapper) {
        this.balanceJpaRepository = balanceJpaRepository;
        this.balanceVOJpaRepository = balanceVOJpaRepository;
        this.balancePersistenceMapper = balancePersistenceMapper;
    }

    @Override
    public Optional<BalanceId> save(BalanceRequestDto balance) {
        Optional<BalanceEntity> balanceEntity = Optional.of(balanceJpaRepository.save(balancePersistenceMapper.toEntity(balance)));
        return Optional.ofNullable(balanceEntity
                        .orElseThrow(() -> new BalancePersistanceException(BALANCE_NOT_SAVED)))
                .map(BalanceEntity::getIdBalance).map(BalanceId::create);
    }

    @Override
    public Optional<BalanceResponseDto> load(Long cardId, CurrencyRequestDto currency) {
        return balanceVOJpaRepository.findActiveByCardId(cardId)
                .map(balanceEntityVO ->
                        balancePersistenceMapper.toDomain(balanceEntityVO, currency));
    }
}
