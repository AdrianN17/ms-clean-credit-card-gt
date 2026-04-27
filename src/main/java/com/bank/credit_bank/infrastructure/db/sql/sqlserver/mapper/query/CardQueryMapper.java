package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.query;

import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.projection.CardSumaryProjection;

@FunctionalInterface
public interface CardQueryMapper {
    LoadCardBalanceBenefitView toView(CardSumaryProjection entity);
}
