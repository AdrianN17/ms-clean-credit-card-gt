package com.bank.credit_bank.infrastructure.presenter.soap.delegate;

import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.*;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.*;

public interface CreditCardDelegateSOAP {

    RetrieveBalanceResponse retrieveBalance(RetrieveBalanceRequest parameters);

    InitiatePaymentResponse initiatePayment(InitiatePaymentRequest parameters);

    InitiateConsumptionResponse initiateConsumption(InitiateConsumptionRequest parameters);

    ExchangeConsumptionResponse exchangeConsumption(ExchangeConsumptionRequest parameters);

    RetrieveConsumptionResponse retrieveConsumption(RetrieveConsumptionRequest parameters);

    ControlConsumptionResponse controlConsumption(ControlConsumptionRequest parameters);

    RetrievePaymentResponse retrievePayment(RetrievePaymentRequest parameters);

    InitiateCardResponse initiateCard(InitiateCardRequest parameters);

    ControlCardResponse controlCard(ControlCardRequest parameters);

    ControlPaymentResponse controlPayment(ControlPaymentRequest parameters);
}

