package com.bank.credit_bank.infrastructure.db.nosql.mongo.adapter;

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
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.PaymentEntityMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance.PaymentPersistanceMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query.PaymentQueryMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.repository.PaymentMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bank.credit_bank.infrastructure.db.nosql.common.constant.TimeConstant.*;
import static com.bank.credit_bank.infrastructure.db.nosql.common.exception.PaymentErrorMessage.*;

public class PaymentMongoRepositoryAdapter implements PaymentDBFindCurrencyPort, PaymentDBSavePort, PaymentFindByIdPort, PaymentsFindByDatesAndCardIdPort {

    private final PaymentMongoRepository paymentMongoRepository;
    private final PaymentPersistanceMapperMongo paymentPersistanceMapperMongo;
    private final PaymentQueryMapperMongo paymentQueryMapperMongo;

    public PaymentMongoRepositoryAdapter(PaymentMongoRepository paymentMongoRepository,
                                         PaymentPersistanceMapperMongo paymentPersistanceMapperMongo,
                                         PaymentQueryMapperMongo paymentQueryMapperMongo) {
        this.paymentMongoRepository = paymentMongoRepository;
        this.paymentPersistanceMapperMongo = paymentPersistanceMapperMongo;
        this.paymentQueryMapperMongo = paymentQueryMapperMongo;
    }

    @Override
    public List<LoadPaymentView> load(FindPaymentByDatesAndCardIdCriteria criteria) {
        return Optional.of(paymentMongoRepository.findByCardIdAndPaymentDateBetween(
                        String.valueOf(criteria.cardId()),
                        criteria.start().atStartOfDay(),
                        criteria.end().atTime(LAST_HOUR, LAST_MINUTE, LAST_SECOND)))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new PaymentPersistanceException(NO_PAYMENTS_FOUND))
                .stream()
                .map(paymentQueryMapperMongo::toView)
                .toList();
    }

    @Override
    public Optional<PaymentId> save(PaymentRequestDto payment) {
        return Optional.of(Optional.of(payment)
                .map(paymentPersistanceMapperMongo::toEntity)
                .map(paymentMongoRepository::save)
                .map(PaymentEntityMongo::getPaymentId)
                .orElseThrow(() -> new PaymentPersistanceException(PAYMENT_NOT_SAVED)))
                .map(PaymentId::new);
    }

    @Override
    public Optional<PaymentResponseDto> load(UUID paymentId, String cardId, CurrencyRequestDto currency) {
        return Optional.of(paymentMongoRepository.findActiveById(paymentId, cardId)
                        .orElseThrow(() -> new PaymentPersistanceException(PAYMENT_NOT_FOUND)))
                .map(payment -> paymentPersistanceMapperMongo.toDomain(payment, currency));
    }

    @Override
    public Optional<String> load(UUID paymentId, String cardId) {
        return Optional.of(paymentMongoRepository.findActiveById(paymentId, cardId)
                        .orElseThrow(() -> new PaymentPersistanceException(PAYMENT_NOT_FOUND)))
                .map(PaymentEntityMongo::getCurrency)
                .map(CurrencyEnum::getCode);
    }
}

