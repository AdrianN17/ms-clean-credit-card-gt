package com.bank.credit_bank.application.payment.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.application.payment.exceptions.ApplicationPaymentException;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPayment;
import com.bank.credit_bank.application.payment.port.in.PaymentCancelUseCase;
import com.bank.credit_bank.application.payment.port.out.PaymentDBFindCurrencyPort;
import com.bank.credit_bank.application.payment.port.out.PaymentFindByIdPort;
import com.bank.credit_bank.application.payment.port.out.PaymentDBSavePort;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_UPDATE_BALANCE;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_NOT_FOUND;
import static com.bank.credit_bank.application.payment.constants.PaymentApplicationErrorMessage.*;

public class PaymentCancelService implements PaymentCancelUseCase {

    private final CardFindByIdPort cardFindByIdPort;
    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final PaymentFindByIdPort paymentFindByIdPort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final PaymentDBSavePort paymentDBSavePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final PaymentDBFindCurrencyPort loadConsumptionCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationPayment mapperApplicationPayment;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final GenericEventPublisherPort genericEventPublisherPort;

    public PaymentCancelService(CardFindByIdPort cardFindByIdPort,
                                BalanceDBFindByIdPort balanceDBFindByIdPort,
                                PaymentFindByIdPort paymentFindByIdPort,
                                BalanceDBSavePort balanceDBSavePort,
                                PaymentDBSavePort paymentDBSavePort,
                                LoadCurrencyPort loadCurrencyPort,
                                CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                PaymentDBFindCurrencyPort loadConsumptionCurrencyPort, MapperApplicationCurrency mapperApplicationCurrency, MapperApplicationPayment mapperApplicationPayment, MapperApplicationCard mapperApplicationCard, MapperApplicationBalance mapperApplicationBalance, GenericEventPublisherPort genericEventPublisherPort) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.paymentFindByIdPort = paymentFindByIdPort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.paymentDBSavePort = paymentDBSavePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.loadConsumptionCurrencyPort = loadConsumptionCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationPayment = mapperApplicationPayment;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }

    @Override
    public PaymentId execute(CardCancelPaymentCommand cardCancelPaymentCommand) {

        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardCancelPaymentCommand.cardId())
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var paymentCurrencyValue = loadConsumptionCurrencyPort
                .load(cardCancelPaymentCommand.paymentId(), cardCancelPaymentCommand.cardId().toString())
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var paymentCurrencyDto = loadCurrencyPort.load(paymentCurrencyValue)
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);
        var paymentCurrency = mapperApplicationCurrency.toDtoRequest(paymentCurrencyDto);

        var paymentResponseDto = paymentFindByIdPort
                .load(cardCancelPaymentCommand.paymentId(), cardCancelPaymentCommand.cardId().toString(), paymentCurrency)
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_NOT_FOUND));

        var cardResponseDto = cardFindByIdPort
                .load(cardCancelPaymentCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardCancelPaymentCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        var payment = mapperApplicationPayment.toDomain(paymentResponseDto);
        var card = mapperApplicationCard.toDomain(cardResponseDto);
        var balance = mapperApplicationBalance.toDomain(balanceResponseDto);

        balance.cancelPayment(payment.getPaymentAmount());
        card.updateStatus(balance.isOvercharged());
        payment.close();

        var paymentRequestDto = mapperApplicationPayment.toDto(payment);
        var balanceRequestDto = mapperApplicationBalance.toDto(balance);

        this.paymentDBSavePort.save(paymentRequestDto).orElseThrow(() -> new ApplicationPaymentException(FAILED_TO_UPDATE_PAYMENT));
        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));

        card.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        balance.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        payment.pullDomainEvents().forEach(genericEventPublisherPort::publish);

        return payment.getId();
    }
}
