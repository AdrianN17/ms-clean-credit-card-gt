package com.bank.credit_bank.application.payment.business;

import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyWSPort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.application.payment.exceptions.ApplicationPaymentException;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPayment;
import com.bank.credit_bank.application.payment.port.out.PaymentDBFindCurrencyPort;
import com.bank.credit_bank.application.payment.port.out.PaymentDBSavePort;
import com.bank.credit_bank.application.payment.port.out.PaymentFindByIdPort;
import com.bank.credit_bank.domain.payment.model.entities.Payment;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import java.util.UUID;

import static com.bank.credit_bank.application.payment.constants.PaymentApplicationErrorMessage.*;

public class BusinessServicePaymentImpl implements BusinessServicePayment {

    private final PaymentFindByIdPort paymentFindByIdPort;
    private final LoadCurrencyWSPort loadCurrencyWSPort;
    private final PaymentDBFindCurrencyPort loadConsumptionCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationPayment mapperApplicationPayment;
    private final PaymentDBSavePort paymentDBSavePort;
    private final GenericEventPublisherPort genericEventPublisherPort;

    public BusinessServicePaymentImpl(PaymentFindByIdPort paymentFindByIdPort, LoadCurrencyWSPort loadCurrencyWSPort, PaymentDBFindCurrencyPort loadConsumptionCurrencyPort, MapperApplicationCurrency mapperApplicationCurrency, MapperApplicationPayment mapperApplicationPayment, PaymentDBSavePort paymentDBSavePort, GenericEventPublisherPort genericEventPublisherPort) {
        this.paymentFindByIdPort = paymentFindByIdPort;
        this.loadCurrencyWSPort = loadCurrencyWSPort;
        this.loadConsumptionCurrencyPort = loadConsumptionCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationPayment = mapperApplicationPayment;
        this.paymentDBSavePort = paymentDBSavePort;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }

    @Override
    public Payment get(Long cardId, UUID paymentId) {
        var paymentCurrencyValue = loadConsumptionCurrencyPort
                .load(paymentId, cardId.toString())
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var paymentCurrencyDto = loadCurrencyWSPort.load(paymentCurrencyValue)
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var paymentCurrency = mapperApplicationCurrency.toDtoRequest(paymentCurrencyDto);

        var paymentResponseDto = paymentFindByIdPort
                .load(paymentId, cardId.toString(), paymentCurrency)
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_NOT_FOUND));

        return mapperApplicationPayment.toDomain(paymentResponseDto);
    }

    @Override
    public PaymentId save(Payment payment) {
        var paymentRequestDto = mapperApplicationPayment.toDto(payment);

        var id = this.paymentDBSavePort.save(paymentRequestDto).orElseThrow(() -> new ApplicationPaymentException(FAILED_TO_CREATE_PAYMENT));

        payment.pullDomainEvents().forEach(genericEventPublisherPort::publish);

        return id;
    }
}
