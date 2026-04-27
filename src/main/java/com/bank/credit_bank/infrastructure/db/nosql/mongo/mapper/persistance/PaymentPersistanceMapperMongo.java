package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.PaymentEntityMongo;

public interface PaymentPersistanceMapperMongo {

    PaymentResponseDto toDomain(PaymentEntityMongo entity, CurrencyRequestDto currency);

    PaymentEntityMongo toEntity(PaymentRequestDto payment);
}
