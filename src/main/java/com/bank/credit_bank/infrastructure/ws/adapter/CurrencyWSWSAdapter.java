package com.bank.credit_bank.infrastructure.ws.adapter;

import com.bank.credit_bank.application.currency.dto.response.CurrencyResponseDto;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import com.bank.credit_bank.infrastructure.ws.mapper.MapperCurrency;
import com.bank.credit_bank.infrastructure.ws.repository.CurrencyJsonServerWSRepository;

import java.util.Optional;

public class CurrencyWSWSAdapter implements LoadCurrencyWSPort {

    private final CurrencyJsonServerWSRepository currencyJsonServerWSRepository;
    private final MapperCurrency mapperCurrency;

    public CurrencyWSWSAdapter(CurrencyJsonServerWSRepository currencyJsonServerWSRepository, MapperCurrency mapperCurrency) {
        this.currencyJsonServerWSRepository = currencyJsonServerWSRepository;
        this.mapperCurrency = mapperCurrency;
    }

    @Override
    public Optional<CurrencyResponseDto> load(String currency) {
        return Optional.of(currencyJsonServerWSRepository.findByCurrency(currency))
                .map(mapperCurrency::toDomain);
    }
}
