package com.bank.credit_bank.infrastructure.presenter.rest.delegate;

import com.bank.credit_bank.application.card.service.CardCloseService;
import com.bank.credit_bank.application.card.service.CardCreateService;
import com.bank.credit_bank.application.card.service.CardFindByIdService;
import com.bank.credit_bank.application.consumption.service.ConsumptionCancelService;
import com.bank.credit_bank.application.consumption.service.ConsumptionFindByDatesAndCardIdService;
import com.bank.credit_bank.application.consumption.service.ConsumptionProcessService;
import com.bank.credit_bank.application.consumption.service.ConsumptionSplitService;
import com.bank.credit_bank.application.payment.queries.FindPaymentByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.payment.service.PaymentCancelService;
import com.bank.credit_bank.application.payment.service.PaymentFindByDatesAndCardIdService;
import com.bank.credit_bank.application.payment.service.PaymentProcessService;
import com.bank.credit_bank.application.consumption.queries.FindConsumptionByDatesAndCardIdCriteria;
import com.bank.credit_bank.infrastructure.presenter.rest.mapper.command.CardApiMapperRequestCommand;
import com.bank.credit_bank.infrastructure.presenter.rest.mapper.command.ConsumptionApiMapperRequestCommand;
import com.bank.credit_bank.infrastructure.presenter.rest.mapper.command.PaymentApiMapperRequestCommand;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.ExchangeConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.InitiateCardRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.InitiateConsumptionRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.InitiatePaymentRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.*;
import com.bank.credit_bank.infrastructure.presenter.rest.util.BindingValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.presenter.rest.util.MapperResponse.*;

@Slf4j
public class CardManagementDelegateImpl implements CardManagementDelegate {

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

    public CardManagementDelegateImpl(CardCreateService cardCreateService,
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
    public ResponseEntity<Long202Response> controlCard(Long cardId) {
        var id = cardCloseService.execute(cardApiMapperRequestCommand.toCommandId(cardId));
        return getLong202Response(id.getValue());
    }

    @Override
    public ResponseEntity<UUID202Response> controlConsumption(Long cardId, UUID consumptionId) {
        var id = consumptionCancelService.execute(consumptionApiMapperRequestCommand.toCommandId(consumptionId, cardId));
        return getUUID202Response(id.getValue());
    }

    @Override
    public ResponseEntity<UUID202Response> controlPayment(Long cardId, UUID paymentId) {
        var id = paymentCancelService.execute(paymentApiMapperRequestCommand.toCommandId(paymentId, cardId));
        return getUUID202Response(id.getValue());
    }

    @Override
    public ResponseEntity<UUID202Response> initiatePayment(Long cardId, InitiatePaymentRequest initiatePaymentRequest, BindingResult bindingResult) {
        BindingValidator.validate(bindingResult);
        var id = paymentProcessService.execute(paymentApiMapperRequestCommand.toCommand(initiatePaymentRequest.getData(), cardId));
        return getUUID202Response(id.getValue());
    }

    @Override
    public ResponseEntity<UUIDList202Response> exchangeConsumption(Long cardId, UUID consumptionId, ExchangeConsumptionRequest exchangeConsumptionRequest, BindingResult bindingResult) {
        BindingValidator.validate(bindingResult);
        var ids = consumptionSplitService.execute(consumptionApiMapperRequestCommand.toCommandIdR(consumptionId, cardId, exchangeConsumptionRequest.getData()));
        return getUUIDList202Response(ids.stream().map(c -> c.getValue()).toList());
    }

    @Override
    public ResponseEntity<Long202Response> initiateCard(InitiateCardRequest initiateCardRequest, BindingResult bindingResult) {
        BindingValidator.validate(bindingResult);
        var id = cardCreateService.execute(cardApiMapperRequestCommand.toCommand(initiateCardRequest.getData()));
        return getLong202Response(id.getValue());
    }

    @Override
    public ResponseEntity<UUID202Response> initiateConsumption(Long cardId, InitiateConsumptionRequest initiateConsumptionRequest, BindingResult bindingResult) {
        BindingValidator.validate(bindingResult);
        var id = consumptionProcessService.execute(consumptionApiMapperRequestCommand.toCommand(initiateConsumptionRequest.getData(), cardId));
        return getUUID202Response(id.getValue());
    }

    @Override
    public ResponseEntity<RetrieveBalance200Response> retrieveBalance(Long cardId) {
        var cardResponse = cardApiMapperRequestCommand.toResponse(cardFindByIdService.execute(cardId));
        return getRetrieveBalance(cardResponse);
    }

    @Override
    public ResponseEntity<RetrieveConsumption200Response> retrieveConsumption(Long cardId, LocalDate dateStart, LocalDate dateEnd) {
        var responseConsumptions = consumptionFindByDatesAndCardIdService
                .execute(new FindConsumptionByDatesAndCardIdCriteria(dateStart, dateEnd, cardId))
                .stream()
                .map(consumptionApiMapperRequestCommand::toResponse)
                .toList();
        return getConsumptionResponse(responseConsumptions);
    }

    @Override
    public ResponseEntity<RetrievePayment200Response> retrievePayment(Long cardId, LocalDate dateStart, LocalDate dateEnd) {
        var responsePayments = paymentFindByDatesAndCardIdService
                .execute(new FindPaymentByDatesAndCardIdCriteria(dateStart, dateEnd, cardId))
                .stream()
                .map(paymentApiMapperRequestCommand::toResponse)
                .toList();
        return getPaymentResponse(responsePayments);
    }
}
