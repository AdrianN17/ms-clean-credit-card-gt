package com.bank.credit_bank.infrastructure.db.sql.sqlserver.adapter;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.application.card.port.out.CardBalanceBenefitFindByIdPort;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.CardEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.projection.CardCurrencyProjection;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.CardPersistanceException;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.CardAccountPersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.CardPersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.query.CardQueryMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.CardAccountJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.CardJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo.CardVOJpaRepository;

import java.util.Optional;

import static com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.CardErrorMessage.*;

public class CardJpaRepositoryAdapter implements CardBalanceBenefitFindByIdPort, CardDBSavePort, CardDBFindCurrencyPort, CardFindByIdPort {

    private final CardJpaRepository cardJpaRepository;
    private final CardVOJpaRepository cardVOJpaRepository;
    private final CardPersistanceMapper cardPersistanceMapper;
    private final CardAccountJpaRepository cardAccountJpaRepository;
    private final CardAccountPersistanceMapper cardAccountPersistanceMapper;
    private final CardQueryMapper cardQueryMapper;

    public CardJpaRepositoryAdapter(CardJpaRepository cardJpaRepository,
                                    CardVOJpaRepository cardVOJpaRepository,
                                    CardPersistanceMapper cardPersistanceMapper,
                                    CardAccountJpaRepository cardAccountJpaRepository,
                                    CardAccountPersistanceMapper cardAccountPersistanceMapper,
                                    CardQueryMapper cardQueryMapper) {
        this.cardJpaRepository = cardJpaRepository;
        this.cardVOJpaRepository = cardVOJpaRepository;
        this.cardPersistanceMapper = cardPersistanceMapper;
        this.cardAccountJpaRepository = cardAccountJpaRepository;
        this.cardAccountPersistanceMapper = cardAccountPersistanceMapper;
        this.cardQueryMapper = cardQueryMapper;
    }


    @Override
    public Optional<CardResponseDto> load(Long cardId, CurrencyRequestDto currency) {
        return Optional.of(cardVOJpaRepository.findActiveById(cardId)
                        .orElseThrow(() -> new CardPersistanceException(CARD_NOT_FOUND)))
                .map(cardVO -> cardPersistanceMapper.toDomain(cardVO, currency));
    }

    @Override
    public Optional<CardId> save(CardRequestDto card) {
        return Optional.of(Optional.ofNullable(card)
                .map(cardPersistanceMapper::toEntity)
                .map(cardJpaRepository::save)
                .map(cardEntity -> {
                    cardAccountJpaRepository.save(cardAccountPersistanceMapper.toEntity(card));
                    return cardEntity;
                })
                .map(CardEntity::getCardId))
                .orElseThrow(() -> new CardPersistanceException(CARD_NOT_SAVED))
                .map(CardId::create);
    }

    @Override
    public Optional<LoadCardBalanceBenefitView> loadAll(Long cardId) {
        return Optional.of(cardVOJpaRepository.getCardAllProjectionByCardId(cardId)
                        .orElseThrow(() -> new CardPersistanceException(NO_CARD_AND_BALANCE_AND_BENEFIT_FOUND)))
                .map(cardQueryMapper::toView);
    }

    @Override
    public Optional<String> load(Long cardId) {
        return Optional.of(cardVOJpaRepository.getCardCurrencyProjectionByCardId(cardId)
                        .orElseThrow(() -> new CardPersistanceException(CARD_NOT_FOUND)))
                .map(CardCurrencyProjection::getCurrencyEnum).map(CurrencyEnum::getCode);
    }
}
