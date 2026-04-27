package com.bank.credit_bank.domain.payment.model.factory;

import com.bank.credit_bank.domain.payment.model.dto.CreatePaymentRequestDto;
import com.bank.credit_bank.domain.payment.model.dto.CreatePaymentRequestFirstDto;
import com.bank.credit_bank.domain.payment.model.entities.Payment;

public interface PaymentFactory {

    Payment create(CreatePaymentRequestDto dto);

    Payment create(CreatePaymentRequestFirstDto dto);
}
