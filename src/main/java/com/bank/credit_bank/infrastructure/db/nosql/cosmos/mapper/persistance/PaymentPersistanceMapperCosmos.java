package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.PaymentEntityCosmos;

public interface PaymentPersistanceMapperCosmos {

    PaymentResponseDto toDomain(PaymentEntityCosmos consumptionEntity, CurrencyRequestDto currency);

    PaymentEntityCosmos toEntity(PaymentRequestDto payment);
}
