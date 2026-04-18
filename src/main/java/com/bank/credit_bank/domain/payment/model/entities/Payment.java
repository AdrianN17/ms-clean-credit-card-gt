package com.bank.credit_bank.domain.payment.model.entities;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Approbation;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.generic.events.DomainEvent;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import java.time.LocalDateTime;
import java.util.List;

public interface Payment {

    Amount getPaymentAmount();

    Approbation getPaymentApprobation();

    CategoryPaymentEnum getCategory();

    CardId getCardId();

    ChannelPaymentEnum getChannelPayment();

    StatusEnum getStatus();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdatedDate();

    PaymentId getId();

    void validateIfPaymentIsPossible(Amount available, Amount total, DateRange dateRange);

    void close();

    List<DomainEvent> pullDomainEvents();

}
