package com.bank.credit_bank.infrastructure.db.nosql.cosmos.adapter;

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
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.ConsumptionEntityCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance.ConsumptionPersistanceMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query.ConsumptionQueryMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.repository.ConsumptionCosmosRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.db.nosql.common.constant.TimeConstant.*;
import static com.bank.credit_bank.infrastructure.db.nosql.common.exception.ConsumptionErrorMessage.*;

public class ConsumptionCosmosRepositoryAdapter implements ConsumptionDBFindCurrencyPort, ConsumptionDBSavePort, ConsumptionFindByIdPort, ConsumptionsDBFindByDatesAndCardIdPort {

    private final ConsumptionCosmosRepository consumptionCosmosRepository;
    private final ConsumptionPersistanceMapperCosmos consumptionPersistanceMapperCosmos;
    private final ConsumptionQueryMapperCosmos consumptionQueryMapperCosmos;

    public ConsumptionCosmosRepositoryAdapter(ConsumptionCosmosRepository consumptionCosmosRepository, ConsumptionPersistanceMapperCosmos consumptionPersistanceMapperCosmos, ConsumptionQueryMapperCosmos consumptionQueryMapperCosmos) {
        this.consumptionCosmosRepository = consumptionCosmosRepository;
        this.consumptionPersistanceMapperCosmos = consumptionPersistanceMapperCosmos;
        this.consumptionQueryMapperCosmos = consumptionQueryMapperCosmos;
    }

    @Override
    public List<LoadConsumptionView> load(FindConsumptionByDatesAndCardIdCriteria criteria) {

        return Optional.of(consumptionCosmosRepository.findByCardIdAndConsumptionDateBetween(
                        String.valueOf(criteria.cardId()),
                        criteria.start().atStartOfDay(),
                        criteria.end().atTime(LAST_HOUR, LAST_MINUTE, LAST_SECOND)))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ConsumptionPersistanceException(NO_CONSUMPTIONS_FOUND))
                .stream()
                .map(consumptionQueryMapperCosmos::toView)
                .toList();

    }

    @Override
    public Optional<ConsumptionId> save(ConsumptionRequestDto consumption) {

        return Optional.of(Optional.of(consumption)
                        .map(consumptionPersistanceMapperCosmos::toEntity)
                        .map(consumptionCosmosRepository::save)
                        .map(ConsumptionEntityCosmos::getConsumptionId)
                        .orElseThrow(() -> new ConsumptionPersistanceException(CONSUMPTION_NOT_SAVED)))
                .map(ConsumptionId::new);

    }

    @Override
    public Optional<ConsumptionResponseDto> load(UUID consumptionId, String cardId, CurrencyRequestDto currency) {
        return Optional.of(consumptionCosmosRepository.findActiveById(consumptionId, cardId)
                        .orElseThrow(() -> new ConsumptionPersistanceException(CONSUMPTION_NOT_FOUND)))
                .map(consumption -> consumptionPersistanceMapperCosmos.toDomain(consumption, currency));
    }

    @Override
    public Optional<String> load(UUID consumptionId, String cardId) {
        return Optional.of(consumptionCosmosRepository.findActiveById(consumptionId, cardId)
                        .orElseThrow(() -> new ConsumptionPersistanceException(CONSUMPTION_NOT_FOUND)))
                .map(ConsumptionEntityCosmos::getCurrency)
                .map(CurrencyEnum::getCode);
    }
}
