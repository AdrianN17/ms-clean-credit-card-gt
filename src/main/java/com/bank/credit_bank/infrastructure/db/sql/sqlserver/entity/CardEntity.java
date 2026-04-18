package com.bank.credit_bank.infrastructure.db.sql.sqlserver.entity;

import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;
import com.bank.credit_bank.infrastructure.db.generic.entity.GenericEntity;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.converter.CategoryCardEnumConverter;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.converter.TypeCardEnumConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Cards")
public class CardEntity extends GenericEntity {

    @Id
    @Column(name = "cardId", nullable = false)
    private Long cardId;

    @Convert(converter = TypeCardEnumConverter.class)
    @Column(name = "typeCard", updatable = false)
    private TypeCardEnum typeCard;

    @Convert(converter = CategoryCardEnumConverter.class)
    @Column(name = "categoryCard", updatable = false)
    private CategoryCardEnum categoryCard;
}
