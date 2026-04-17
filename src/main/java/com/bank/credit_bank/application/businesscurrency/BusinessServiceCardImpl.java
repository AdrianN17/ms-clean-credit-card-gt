package com.bank.credit_bank.application.businesscurrency;

import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;

import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.*;

public class BusinessServiceCardImpl implements BusinessServiceCard {

    private final CardFindByIdPort cardFindByIdPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBSavePort cardDBSavePort;
    private final GenericEventPublisherPort genericEventPublisherPort;


    public BusinessServiceCardImpl(CardFindByIdPort cardFindByIdPort, CardDBFindCurrencyPort cardDBFindCurrencyPort, MapperApplicationCard mapperApplicationCard, MapperApplicationCurrency mapperApplicationCurrency, LoadCurrencyPort loadCurrencyPort, CardDBSavePort cardDBSavePort, GenericEventPublisherPort genericEventPublisherPort) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBSavePort = cardDBSavePort;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }

    @Override
    public Card get(Long cardId) {
        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardId)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);

        var cardResponseDto = cardFindByIdPort
                .load(cardId, cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        return mapperApplicationCard.toDomain(cardResponseDto);
    }

    @Override
    public CardId save(Card card) {
        var cardRequestDto = mapperApplicationCard.toDto(card);

        var id = cardDBSavePort.save(cardRequestDto)
                .orElseThrow(() -> new ApplicationCardException(FAILED_TO_CREATE_CARD));

        card.pullDomainEvents().forEach(genericEventPublisherPort::publish);

        return id;
    }
}
