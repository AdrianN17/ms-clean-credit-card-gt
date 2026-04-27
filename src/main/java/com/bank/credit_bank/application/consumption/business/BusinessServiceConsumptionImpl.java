package com.bank.credit_bank.application.consumption.business;

import com.bank.credit_bank.application.consumption.exceptions.ApplicationConsumptionException;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumption;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBFindCurrencyPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import java.util.UUID;

import static com.bank.credit_bank.application.consumption.constants.ConsumptionApplicationErrorMessage.*;

public class BusinessServiceConsumptionImpl implements BusinessServiceConsumption {

    private final ConsumptionFindByIdPort consumptionFindByIdPort;
    private final ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort;
    private final LoadCurrencyWSPort loadCurrencyWSPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationConsumption mapperApplicationConsumption;
    private final ConsumptionDBSavePort consumptionDBSavePort;
    private final GenericEventPublisherPort genericEventPublisherPort;

    public BusinessServiceConsumptionImpl(ConsumptionFindByIdPort consumptionFindByIdPort, ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort, LoadCurrencyWSPort loadCurrencyWSPort, MapperApplicationCurrency mapperApplicationCurrency, MapperApplicationConsumption mapperApplicationConsumption, ConsumptionDBSavePort consumptionDBSavePort, GenericEventPublisherPort genericEventPublisherPort) {
        this.consumptionFindByIdPort = consumptionFindByIdPort;
        this.consumptionDBFindCurrencyPort = consumptionDBFindCurrencyPort;
        this.loadCurrencyWSPort = loadCurrencyWSPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationConsumption = mapperApplicationConsumption;
        this.consumptionDBSavePort = consumptionDBSavePort;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }

    @Override
    public Consumption get(Long cardId, UUID consumptionId) {
        var consumptionCurrencyValue = consumptionDBFindCurrencyPort
                .load(consumptionId, cardId.toString())
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_NOT_FOUND));

        var consumptionCurrencyDto = loadCurrencyWSPort.load(consumptionCurrencyValue)
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_CURRENCY_NOT_FOUND));

        var consumptionCurrency = mapperApplicationCurrency.toDtoRequest(consumptionCurrencyDto);

        var consumptionResponseDto = consumptionFindByIdPort
                .load(consumptionId, cardId.toString(), consumptionCurrency)
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_NOT_FOUND));

        return mapperApplicationConsumption.toDomain(consumptionResponseDto);
    }

    @Override
    public ConsumptionId save(Consumption consumption) {
        var consumptionRequestDto = mapperApplicationConsumption.toDto(consumption);

        var id = this.consumptionDBSavePort.save(consumptionRequestDto).orElseThrow(() ->
                new ApplicationConsumptionException(FAILED_TO_CREATE_CONSUMPTION));

        consumption.pullDomainEvents().forEach(genericEventPublisherPort::publish);

        return id;
    }
}
