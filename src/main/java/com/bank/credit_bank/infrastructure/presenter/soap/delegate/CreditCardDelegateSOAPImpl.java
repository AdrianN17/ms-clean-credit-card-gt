package com.bank.credit_bank.infrastructure.presenter.soap.delegate;

import com.bank.credit_bank.application.card.service.CardCloseService;
import com.bank.credit_bank.application.card.service.CardCreateService;
import com.bank.credit_bank.application.card.service.CardFindByIdService;
import com.bank.credit_bank.application.consumption.queries.FindConsumptionByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.consumption.service.ConsumptionCancelService;
import com.bank.credit_bank.application.consumption.service.ConsumptionFindByDatesAndCardIdService;
import com.bank.credit_bank.application.consumption.service.ConsumptionProcessService;
import com.bank.credit_bank.application.consumption.service.ConsumptionSplitService;
import com.bank.credit_bank.application.payment.queries.FindPaymentByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.payment.service.PaymentCancelService;
import com.bank.credit_bank.application.payment.service.PaymentFindByDatesAndCardIdService;
import com.bank.credit_bank.application.payment.service.PaymentProcessService;
import com.bank.credit_bank.infrastructure.presenter.soap.mapper.CardApiMapperRequestCommand;
import com.bank.credit_bank.infrastructure.presenter.soap.mapper.ConsumptionApiMapperRequestCommand;
import com.bank.credit_bank.infrastructure.presenter.soap.mapper.PaymentApiMapperRequestCommand;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.*;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.*;

import java.time.LocalDate;

import static com.bank.credit_bank.infrastructure.presenter.soap.util.SoapMapperResponse.buildTracking;

public class CreditCardDelegateSOAPImpl implements CreditCardDelegateSOAP {

    private final CardCreateService cardCreateService;
    private final CardCloseService cardCloseService;
    private final CardFindByIdService cardFindByIdService;
    private final PaymentProcessService paymentProcessService;
    private final PaymentCancelService paymentCancelService;
    private final PaymentFindByDatesAndCardIdService paymentFindByDatesAndCardIdService;
    private final ConsumptionProcessService consumptionProcessService;
    private final ConsumptionCancelService consumptionCancelService;
    private final ConsumptionSplitService consumptionSplitService;
    private final ConsumptionFindByDatesAndCardIdService consumptionFindByDatesAndCardIdService;
    private final CardApiMapperRequestCommand cardApiMapperRequestCommand;
    private final ConsumptionApiMapperRequestCommand consumptionApiMapperRequestCommand;
    private final PaymentApiMapperRequestCommand paymentApiMapperRequestCommand;

    public CreditCardDelegateSOAPImpl(CardCreateService cardCreateService,
                                      CardCloseService cardCloseService,
                                      CardFindByIdService cardFindByIdService,
                                      PaymentProcessService paymentProcessService,
                                      PaymentCancelService paymentCancelService,
                                      PaymentFindByDatesAndCardIdService paymentFindByDatesAndCardIdService,
                                      ConsumptionProcessService consumptionProcessService,
                                      ConsumptionCancelService consumptionCancelService,
                                      ConsumptionSplitService consumptionSplitService,
                                      ConsumptionFindByDatesAndCardIdService consumptionFindByDatesAndCardIdService,
                                      CardApiMapperRequestCommand cardApiMapperRequestCommand,
                                      ConsumptionApiMapperRequestCommand consumptionApiMapperRequestCommand,
                                      PaymentApiMapperRequestCommand paymentApiMapperRequestCommand) {
        this.cardCreateService = cardCreateService;
        this.cardCloseService = cardCloseService;
        this.cardFindByIdService = cardFindByIdService;
        this.paymentProcessService = paymentProcessService;
        this.paymentCancelService = paymentCancelService;
        this.paymentFindByDatesAndCardIdService = paymentFindByDatesAndCardIdService;
        this.consumptionProcessService = consumptionProcessService;
        this.consumptionCancelService = consumptionCancelService;
        this.consumptionSplitService = consumptionSplitService;
        this.consumptionFindByDatesAndCardIdService = consumptionFindByDatesAndCardIdService;
        this.cardApiMapperRequestCommand = cardApiMapperRequestCommand;
        this.consumptionApiMapperRequestCommand = consumptionApiMapperRequestCommand;
        this.paymentApiMapperRequestCommand = paymentApiMapperRequestCommand;
    }

    @Override
    public RetrieveBalanceResponse retrieveBalance(RetrieveBalanceRequest parameters) {
        var view = cardFindByIdService.execute(parameters.getCardId());
        var cardResponse = cardApiMapperRequestCommand.toResponse(view);

        RetrieveBalanceResponse response = new RetrieveBalanceResponse();
        response.setData(cardResponse);
        response.setTracking(buildTracking());
        return response;
    }

    @Override
    public InitiatePaymentResponse initiatePayment(InitiatePaymentRequest parameters) {
        var id = paymentProcessService.execute(paymentApiMapperRequestCommand.toCommand(parameters));

        UUIDResponse uuidResponse = new UUIDResponse();
        uuidResponse.setId(id.getValue().toString());

        InitiatePaymentResponse response = new InitiatePaymentResponse();
        response.setTracking(buildTracking());
        response.setData(uuidResponse);
        return response;
    }

    @Override
    public InitiateConsumptionResponse initiateConsumption(InitiateConsumptionRequest parameters) {
        var id = consumptionProcessService.execute(consumptionApiMapperRequestCommand.toCommand(parameters));

        UUIDResponse uuidResponse = new UUIDResponse();
        uuidResponse.setId(id.getValue().toString());

        InitiateConsumptionResponse response = new InitiateConsumptionResponse();
        response.setTracking(buildTracking());
        response.setData(uuidResponse);
        return response;
    }

    @Override
    public ExchangeConsumptionResponse exchangeConsumption(ExchangeConsumptionRequest parameters) {
        var ids = consumptionSplitService.execute(consumptionApiMapperRequestCommand.toCommandIdR(parameters));

        UUIDResponseList uuidResponseList = new UUIDResponseList();
        ids.forEach(consumptionId -> {
            UUIDResponse uuidResponse = new UUIDResponse();
            uuidResponse.setId(consumptionId.getValue().toString());
            uuidResponseList.getItem().add(uuidResponse);
        });

        ExchangeConsumptionResponse response = new ExchangeConsumptionResponse();
        response.setTracking(buildTracking());
        response.setData(uuidResponseList);
        return response;
    }

    @Override
    public RetrieveConsumptionResponse retrieveConsumption(RetrieveConsumptionRequest parameters) {
        LocalDate dateStart = parameters.getDateStart().toGregorianCalendar().toZonedDateTime().toLocalDate();
        LocalDate dateEnd = parameters.getDateEnd().toGregorianCalendar().toZonedDateTime().toLocalDate();

        var consumptionResponseList = new ConsumptionResponseList();
        consumptionFindByDatesAndCardIdService
                .execute(new FindConsumptionByDatesAndCardIdCriteria(dateStart, dateEnd, parameters.getCardId()))
                .forEach(view -> consumptionResponseList.getItem()
                        .addAll(consumptionApiMapperRequestCommand.toResponse(view).getItem()));

        RetrieveConsumptionResponse response = new RetrieveConsumptionResponse();
        response.setTracking(buildTracking());
        response.setData(consumptionResponseList);
        return response;
    }

    @Override
    public ControlConsumptionResponse controlConsumption(ControlConsumptionRequest parameters) {
        var id = consumptionCancelService.execute(consumptionApiMapperRequestCommand.toCommandId(parameters));

        UUIDResponse uuidResponse = new UUIDResponse();
        uuidResponse.setId(id.getValue().toString());

        ControlConsumptionResponse response = new ControlConsumptionResponse();
        response.setTracking(buildTracking());
        response.setData(uuidResponse);
        return response;
    }

    @Override
    public RetrievePaymentResponse retrievePayment(RetrievePaymentRequest parameters) {
        LocalDate dateStart = parameters.getDateStart().toGregorianCalendar().toZonedDateTime().toLocalDate();
        LocalDate dateEnd = parameters.getDateEnd().toGregorianCalendar().toZonedDateTime().toLocalDate();

        var paymentResponseList = new PaymentResponseList();
        paymentFindByDatesAndCardIdService
                .execute(new FindPaymentByDatesAndCardIdCriteria(dateStart, dateEnd, parameters.getCardId()))
                .forEach(view -> paymentResponseList.getItem()
                        .addAll(paymentApiMapperRequestCommand.toResponse(view).getItem()));

        RetrievePaymentResponse response = new RetrievePaymentResponse();
        response.setTracking(buildTracking());
        response.setData(paymentResponseList);
        return response;
    }

    @Override
    public InitiateCardResponse initiateCard(InitiateCardRequest parameters) {
        var id = cardCreateService.execute(cardApiMapperRequestCommand.toCommand(parameters));

        LongResponse longResponse = new LongResponse();
        longResponse.setId(id.getValue());

        InitiateCardResponse response = new InitiateCardResponse();
        response.setTracking(buildTracking());
        response.setData(longResponse);
        return response;
    }

    @Override
    public ControlCardResponse controlCard(ControlCardRequest parameters) {
        var id = cardCloseService.execute(cardApiMapperRequestCommand.toCommandId(parameters));

        LongResponse longResponse = new LongResponse();
        longResponse.setId(id.getValue());

        ControlCardResponse response = new ControlCardResponse();
        response.setTracking(buildTracking());
        response.setData(longResponse);
        return response;
    }

    @Override
    public ControlPaymentResponse controlPayment(ControlPaymentRequest parameters) {
        var id = paymentCancelService.execute(paymentApiMapperRequestCommand.toCommandId(parameters));

        UUIDResponse uuidResponse = new UUIDResponse();
        uuidResponse.setId(id.getValue().toString());

        ControlPaymentResponse response = new ControlPaymentResponse();
        response.setTracking(buildTracking());
        response.setData(uuidResponse);
        return response;
    }

}

