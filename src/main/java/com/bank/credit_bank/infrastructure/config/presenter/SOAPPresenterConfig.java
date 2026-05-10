package com.bank.credit_bank.infrastructure.config.presenter;

import com.bank.credit_bank.application.card.service.CardCloseService;
import com.bank.credit_bank.application.card.service.CardCreateService;
import com.bank.credit_bank.application.card.service.CardFindByIdService;
import com.bank.credit_bank.application.consumption.service.ConsumptionCancelService;
import com.bank.credit_bank.application.consumption.service.ConsumptionFindByDatesAndCardIdService;
import com.bank.credit_bank.application.consumption.service.ConsumptionProcessService;
import com.bank.credit_bank.application.consumption.service.ConsumptionSplitService;
import com.bank.credit_bank.application.payment.service.PaymentCancelService;
import com.bank.credit_bank.application.payment.service.PaymentFindByDatesAndCardIdService;
import com.bank.credit_bank.application.payment.service.PaymentProcessService;
import com.bank.credit_bank.infrastructure.presenter.soap.delegate.CreditCardDelegateSOAP;
import com.bank.credit_bank.infrastructure.presenter.soap.delegate.CreditCardDelegateSOAPImpl;
import com.bank.credit_bank.infrastructure.presenter.soap.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SOAPPresenterConfig {

    @Bean
    public CreditCardDelegateSOAP creditCardDelegateSOAP(CardCreateService cardCreateService,
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
        return new CreditCardDelegateSOAPImpl(cardCreateService,
                cardCloseService,
                cardFindByIdService,
                paymentProcessService,
                paymentCancelService,
                paymentFindByDatesAndCardIdService,
                consumptionProcessService,
                consumptionCancelService,
                consumptionSplitService,
                consumptionFindByDatesAndCardIdService,
                cardApiMapperRequestCommand,
                consumptionApiMapperRequestCommand,
                paymentApiMapperRequestCommand);
    }

    @Bean
    public CardApiMapperRequestCommand cardApiMapperRequestCommand() {
        return new CardApiMapperRequestCommandImpl();
    }

    @Bean
    public ConsumptionApiMapperRequestCommand consumptionApiMapperRequestCommand() {
        return new ConsumptionApiMapperRequestCommandImpl();
    }

    @Bean
    public PaymentApiMapperRequestCommand paymentApiMapperRequestCommand() {
        return new PaymentApiMapperRequestCommandImpl();
    }
}
