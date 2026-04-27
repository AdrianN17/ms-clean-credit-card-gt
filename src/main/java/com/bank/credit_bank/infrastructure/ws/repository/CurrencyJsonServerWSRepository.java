package com.bank.credit_bank.infrastructure.ws.repository;

import com.bank.credit_bank.infrastructure.ws.dto.CurrencyDto;
import com.bank.credit_bank.infrastructure.ws.exception.ConverterWSClientException;
import org.springframework.web.client.RestClient;

public class CurrencyJsonServerWSRepository {

    private final RestClient restClient;

    public CurrencyJsonServerWSRepository(RestClient restClient) {
        this.restClient = restClient;
    }

    public CurrencyDto findByCurrency(String currency) {
        try {
            return restClient
                    .get()
                    .uri("/" + currency)
                    .retrieve()
                    .body(CurrencyDto.class);
        } catch (Exception e) {
            throw new ConverterWSClientException(e);
        }
    }
}