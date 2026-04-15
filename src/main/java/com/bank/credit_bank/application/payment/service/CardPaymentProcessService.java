package com.bank.credit_bank.application.payment.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBFindByIdPort;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.application.payment.exceptions.ApplicationPaymentException;
import com.bank.credit_bank.application.payment.mapper.MapperApplicationPayment;
import com.bank.credit_bank.application.payment.port.in.PaymentProcessUseCase;
import com.bank.credit_bank.application.payment.port.out.PaymentDBSavePort;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.payment.model.entities.Payment;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_UPDATE_BALANCE;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.BENEFIT_NOT_FOUND;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.FAILED_TO_UPDATE_BENEFIT;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_NOT_FOUND;
import static com.bank.credit_bank.application.payment.constants.PaymentApplicationErrorMessage.FAILED_TO_CREATE_PAYMENT;
import static com.bank.credit_bank.application.payment.constants.PaymentApplicationErrorMessage.PAYMENT_CURRENCY_NOT_FOUND;
import static java.util.Objects.isNull;

public class CardPaymentProcessService implements PaymentProcessUseCase {

    private final CardFindByIdPort cardFindByIdPort;
    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final BenefitDBFindByIdPort benefitDBFindByIdPort;
    private final BenefitDBSavePort benefitDBSavePort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final PaymentDBSavePort paymentDBSavePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationPayment mapperApplicationPayment;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final MapperApplicationBenefit mapperApplicationBenefit;

    public CardPaymentProcessService(CardFindByIdPort cardFindByIdPort, BalanceDBFindByIdPort balanceDBFindByIdPort, BenefitDBFindByIdPort benefitDBFindByIdPort, BenefitDBSavePort benefitDBSavePort, BalanceDBSavePort balanceDBSavePort, PaymentDBSavePort paymentDBSavePort, LoadCurrencyPort loadCurrencyPort, CardDBFindCurrencyPort cardDBFindCurrencyPort, MapperApplicationCurrency mapperApplicationCurrency, MapperApplicationPayment mapperApplicationPayment, MapperApplicationCard mapperApplicationCard, MapperApplicationBalance mapperApplicationBalance, MapperApplicationBenefit mapperApplicationBenefit) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.benefitDBFindByIdPort = benefitDBFindByIdPort;
        this.benefitDBSavePort = benefitDBSavePort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.paymentDBSavePort = paymentDBSavePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationPayment = mapperApplicationPayment;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.mapperApplicationBenefit = mapperApplicationBenefit;
    }

    @Override
    public PaymentId execute(CardProcessPaymentCommand cardProcessPaymentCommand) {

        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardProcessPaymentCommand.cardId())
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var paymentCurrencyDto = loadCurrencyPort.load(cardProcessPaymentCommand.currency())
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);
        var paymentCurrency = mapperApplicationCurrency.toDtoRequest(paymentCurrencyDto);

        var cardResponseDto = cardFindByIdPort
                .load(cardProcessPaymentCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardProcessPaymentCommand.cardId(), paymentCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        var benefitResponseDto = benefitDBFindByIdPort
                .load(cardProcessPaymentCommand.cardId())
                .orElseThrow(() -> new ApplicationBenefitException(BENEFIT_NOT_FOUND));

        var card = mapperApplicationCard.toDomain(cardResponseDto);
        var balance = mapperApplicationBalance.toDomain(balanceResponseDto);

        Payment payment = Payment.builder()
                .paymentAmount(Amount.create(Currency.create(CurrencyEnum.ofValue(paymentCurrency.currency()).orElseThrow(), paymentCurrency.exchangeRate()), cardProcessPaymentCommand.amount()))
                .category(CategoryPaymentEnum.ofValue(cardProcessPaymentCommand.category()).orElseThrow())
                .cardId(CardId.create(cardProcessPaymentCommand.cardId()))
                .channelPayment(ChannelPaymentEnum.ofValue(cardProcessPaymentCommand.channelPayment()).orElseThrow())
                .build();

        if (isNull(cardProcessPaymentCommand.pointsUsed())) {
            card.pay(balance, payment);
        } else {
            var benefit = mapperApplicationBenefit.toDomain(benefitResponseDto);


            Point point = Point.create(cardProcessPaymentCommand.pointsUsed());
            card.pay(balance, benefit, payment, point);

            var benefitRequestDto = mapperApplicationBenefit.toDto(benefit);
            this.benefitDBSavePort.save(benefitRequestDto).orElseThrow(() -> new ApplicationBenefitException(FAILED_TO_UPDATE_BENEFIT));
        }

        var paymentRequestDto = mapperApplicationPayment.toDto(payment);
        var balanceRequestDto = mapperApplicationBalance.toDto(balance);

        this.paymentDBSavePort.save(paymentRequestDto).orElseThrow(() -> new ApplicationPaymentException(FAILED_TO_CREATE_PAYMENT));
        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));

        return payment.getId();
    }
}
