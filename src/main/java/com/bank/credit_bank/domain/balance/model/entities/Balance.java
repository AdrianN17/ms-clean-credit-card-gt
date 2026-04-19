package com.bank.credit_bank.domain.balance.model.entities;

import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface Balance {
    CardId getCardId();

    Amount getTotal();

    Amount getOld();

    DateRange getDateRange();

    Amount getAvailable();

    StatusEnum getStatus();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdatedDate();

    BalanceId getId();

    List<DomainEvent> pullDomainEvents();

    void close();
}
