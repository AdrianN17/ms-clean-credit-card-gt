package com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.query;

import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity.projection.CardSumaryProjection;
import org.springframework.stereotype.Component;

@Component
public class CardQueryMapperImpl implements CardQueryMapper {

    @Override
    public LoadCardBalanceBenefitView toView(CardSumaryProjection entity) {

        return new LoadCardBalanceBenefitView(
                entity.getTypeCardEnum().getCode(),
                entity.getCategoryCardEnum().getCode(),
                entity.getCreditTotal(),
                entity.getCurrencyEnum().getCode(),
                entity.getDebtTax(),
                entity.getCardStatusEnum().getCode(),
                entity.getPaymentDate().shortValue(),
                entity.getTotalPoints(),
                entity.getHasDiscount(),
                entity.getMultiplierPoints(),
                entity.getTotalAmount(),
                entity.getOldAmount(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getAvailableAmount()

        );
    }
}
