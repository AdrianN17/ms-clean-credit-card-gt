package com.bank.credit_bank.infrastructure.db.nosql.cosmos.adapter;

import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.application.payment.dto.request.PaymentRequestDto;
import com.bank.credit_bank.application.payment.dto.response.PaymentResponseDto;
import com.bank.credit_bank.application.payment.port.out.PaymentDBFindCurrencyPort;
import com.bank.credit_bank.application.payment.port.out.PaymentDBSavePort;
import com.bank.credit_bank.application.payment.port.out.PaymentFindByIdPort;
import com.bank.credit_bank.application.payment.port.out.PaymentsFindByDatesAndCardIdPort;
import com.bank.credit_bank.application.payment.queries.FindPaymentByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;
import com.bank.credit_bank.infrastructure.db.nosql.common.exception.PaymentPersistanceException;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.PaymentEntityCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance.PaymentPersistanceMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query.PaymentQueryMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.repository.PaymentCosmosRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.db.nosql.common.constant.TimeConstant.*;
import static com.bank.credit_bank.infrastructure.db.nosql.common.exception.PaymentErrorMessage.*;

public class PaymentCosmosRepositoryAdapter implements PaymentDBFindCurrencyPort, PaymentDBSavePort, PaymentFindByIdPort, PaymentsFindByDatesAndCardIdPort {

    private final PaymentCosmosRepository paymentCosmosRepository;
    private final PaymentPersistanceMapperCosmos paymentPersistanceMapperCosmos;
    private final PaymentQueryMapperCosmos paymentQueryMapperCosmos;

    public PaymentCosmosRepositoryAdapter(PaymentCosmosRepository paymentCosmosRepository,
                                          PaymentPersistanceMapperCosmos paymentPersistanceMapperCosmos,
                                          PaymentQueryMapperCosmos paymentQueryMapperCosmos) {
        this.paymentCosmosRepository = paymentCosmosRepository;
        this.paymentPersistanceMapperCosmos = paymentPersistanceMapperCosmos;
        this.paymentQueryMapperCosmos = paymentQueryMapperCosmos;
    }

    @Override
    public List<LoadPaymentView> load(FindPaymentByDatesAndCardIdCriteria criteria) {
        return Optional.of(paymentCosmosRepository.findByCardIdAndPaymentDateBetween(
                        String.valueOf(criteria.cardId()),
                        criteria.start().atStartOfDay(),
                        criteria.end().atTime(LAST_HOUR, LAST_MINUTE, LAST_SECOND)))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new PaymentPersistanceException(NO_PAYMENTS_FOUND))
                .stream()
                .map(paymentQueryMapperCosmos::toView)
                .toList();
    }

    @Override
    public Optional<PaymentId> save(PaymentRequestDto payment) {
        return Optional.of(Optional.of(payment)
                .map(paymentPersistanceMapperCosmos::toEntity)
                .map(paymentCosmosRepository::save)
                .map(PaymentEntityCosmos::getPaymentId)
                .orElseThrow(() -> new PaymentPersistanceException(PAYMENT_NOT_SAVED)))
                .map(PaymentId::new);
    }

    @Override
    public Optional<PaymentResponseDto> load(UUID paymentId, String cardId, CurrencyRequestDto currency) {
        return Optional.of(paymentCosmosRepository.findActiveById(paymentId, cardId)
                        .orElseThrow(() -> new PaymentPersistanceException(PAYMENT_NOT_FOUND)))
                .map(payment -> paymentPersistanceMapperCosmos.toDomain(payment, currency));
    }

    @Override
    public Optional<String> load(UUID paymentId, String cardId) {
        return Optional.of(paymentCosmosRepository.findActiveById(paymentId, cardId)
                        .orElseThrow(() -> new PaymentPersistanceException(PAYMENT_NOT_FOUND)))
                .map(PaymentEntityCosmos::getCurrency)
                .map(CurrencyEnum::getCode);
    }
}

