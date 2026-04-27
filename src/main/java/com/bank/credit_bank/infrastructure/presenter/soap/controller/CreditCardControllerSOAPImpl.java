package com.bank.credit_bank.infrastructure.presenter.soap.controller;

import com.bank.credit_bank.infrastructure.presenter.soap.delegate.CreditCardDelegateSOAP;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.*;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.*;
import jakarta.jws.WebService;

@WebService(serviceName = "CreditCardService",
        portName = "CreditCardPort",
        targetNamespace = "http://bank.com/credit_card",
        endpointInterface = "com.bank.credit_bank.infrastructure.presenter.soap.controller.CreditCardControllerSOAP")
public class CreditCardControllerSOAPImpl implements CreditCardControllerSOAP {

    private final CreditCardDelegateSOAP creditCardDelegateSOAP;

    public CreditCardControllerSOAPImpl(CreditCardDelegateSOAP creditCardDelegateSOAP) {
        this.creditCardDelegateSOAP = creditCardDelegateSOAP;
    }

    @Override
    public RetrieveBalanceResponse retrieveBalance(RetrieveBalanceRequest parameters) {
        return creditCardDelegateSOAP.retrieveBalance(parameters);
    }

    @Override
    public InitiatePaymentResponse initiatePayment(InitiatePaymentRequest parameters) {
        return creditCardDelegateSOAP.initiatePayment(parameters);
    }

    @Override
    public InitiateConsumptionResponse initiateConsumption(InitiateConsumptionRequest parameters) {
        return creditCardDelegateSOAP.initiateConsumption(parameters);
    }

    @Override
    public ExchangeConsumptionResponse exchangeConsumption(ExchangeConsumptionRequest parameters) {
        return creditCardDelegateSOAP.exchangeConsumption(parameters);
    }

    @Override
    public RetrieveConsumptionResponse retrieveConsumption(RetrieveConsumptionRequest parameters) {
        return creditCardDelegateSOAP.retrieveConsumption(parameters);
    }

    @Override
    public ControlConsumptionResponse controlConsumption(ControlConsumptionRequest parameters) {
        return creditCardDelegateSOAP.controlConsumption(parameters);
    }

    @Override
    public RetrievePaymentResponse retrievePayment(RetrievePaymentRequest parameters) {
        return creditCardDelegateSOAP.retrievePayment(parameters);
    }

    @Override
    public InitiateCardResponse initiateCard(InitiateCardRequest parameters) {
        return creditCardDelegateSOAP.initiateCard(parameters);
    }

    @Override
    public ControlCardResponse controlCard(ControlCardRequest parameters) {
        return creditCardDelegateSOAP.controlCard(parameters);
    }

    @Override
    public ControlPaymentResponse controlPayment(ControlPaymentRequest parameters) {
        return creditCardDelegateSOAP.controlPayment(parameters);
    }
}

