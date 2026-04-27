package com.bank.credit_bank.infrastructure.db.nosql.mongo.adapter;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBFindCurrencyPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionFindByIdPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionsDBFindByDatesAndCardIdPort;
import com.bank.credit_bank.application.consumption.queries.FindConsumptionByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.consumption.view.LoadConsumptionView;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;
import com.bank.credit_bank.infrastructure.db.nosql.common.exception.ConsumptionPersistanceException;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.ConsumptionEntityMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance.ConsumptionPersistanceMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query.ConsumptionQueryMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.repository.ConsumptionMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.db.nosql.common.constant.TimeConstant.*;
import static com.bank.credit_bank.infrastructure.db.nosql.common.exception.ConsumptionErrorMessage.*;

public class ConsumptionMongoRepositoryAdapter implements ConsumptionDBFindCurrencyPort, ConsumptionDBSavePort, ConsumptionFindByIdPort, ConsumptionsDBFindByDatesAndCardIdPort {

    private final ConsumptionMongoRepository consumptionMongoRepository;
    private final ConsumptionPersistanceMapperMongo consumptionPersistanceMapperMongo;
    private final ConsumptionQueryMapperMongo consumptionQueryMapperMongo;

    public ConsumptionMongoRepositoryAdapter(ConsumptionMongoRepository consumptionMongoRepository,
                                             ConsumptionPersistanceMapperMongo consumptionPersistanceMapperMongo,
                                             ConsumptionQueryMapperMongo consumptionQueryMapperMongo) {
        this.consumptionMongoRepository = consumptionMongoRepository;
        this.consumptionPersistanceMapperMongo = consumptionPersistanceMapperMongo;
        this.consumptionQueryMapperMongo = consumptionQueryMapperMongo;
    }

    @Override
    public List<LoadConsumptionView> load(FindConsumptionByDatesAndCardIdCriteria criteria) {
        return Optional.of(consumptionMongoRepository.findByCardIdAndConsumptionDateBetween(
                        String.valueOf(criteria.cardId()),
                        criteria.start().atStartOfDay(),
                        criteria.end().atTime(LAST_HOUR, LAST_MINUTE, LAST_SECOND)))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ConsumptionPersistanceException(NO_CONSUMPTIONS_FOUND))
                .stream()
                .map(consumptionQueryMapperMongo::toView)
                .toList();
    }

    @Override
    public Optional<ConsumptionId> save(ConsumptionRequestDto consumption) {
        return Optional.of(Optional.of(consumption)
                        .map(consumptionPersistanceMapperMongo::toEntity)
                        .map(consumptionMongoRepository::save)
                        .map(ConsumptionEntityMongo::getConsumptionId)
                        .orElseThrow(() -> new ConsumptionPersistanceException(CONSUMPTION_NOT_SAVED)))
                .map(ConsumptionId::new);
    }

    @Override
    public Optional<ConsumptionResponseDto> load(UUID consumptionId, String cardId, CurrencyRequestDto currency) {
        return Optional.of(consumptionMongoRepository.findActiveById(consumptionId, cardId)
                        .orElseThrow(() -> new ConsumptionPersistanceException(CONSUMPTION_NOT_FOUND)))
                .map(consumption -> consumptionPersistanceMapperMongo.toDomain(consumption, currency));
    }

    @Override
    public Optional<String> load(UUID consumptionId, String cardId) {
        return Optional.of(consumptionMongoRepository.findActiveById(consumptionId, cardId)
                        .orElseThrow(() -> new ConsumptionPersistanceException(CONSUMPTION_NOT_FOUND)))
                .map(ConsumptionEntityMongo::getCurrency)
                .map(CurrencyEnum::getCode);
    }
}

