package com.example.bankcards.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.example.bankcards.dto.FilterPageCardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.QCard;
import com.example.bankcards.entity.STATUS;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCard card = QCard.card;

    public CardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Card> findByCriteria(String username, String cardNumber, STATUS status,
                                    Double minBalance, Double maxBalance, LocalDate minEndDate,
                                    LocalDate maxEndDate, Boolean isAdminSearch, Pageable pageable) {
        BooleanBuilder whereClause = buildWhereClause(username, cardNumber, status, 
                                                     minBalance, maxBalance, minEndDate, 
                                                     maxEndDate, isAdminSearch);
        
        long total = countByCriteria(whereClause);
        
        if (total == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        
        List<Card> cards = findCardsByCriteria(whereClause, pageable);
        
        return new PageImpl<>(cards, pageable, total);
    }
    
    private BooleanBuilder buildWhereClause(String username, String cardNumber, STATUS status,
                                           Double minBalance, Double maxBalance, LocalDate minEndDate,
                                           LocalDate maxEndDate, Boolean isAdminSearch) {
        BooleanBuilder whereClause = new BooleanBuilder();
        
        if (Boolean.TRUE.equals(isAdminSearch)) {
            if (username != null && !username.isEmpty()) {
                whereClause.and(card.owner().username.containsIgnoreCase(username));
            }
        } else {
            whereClause.and(card.owner().username.eq(username));
        }
        
        if (cardNumber != null && !cardNumber.isEmpty()) {
            whereClause.and(card.cardNumber.containsIgnoreCase(cardNumber));
        }
        
        if (status != null) {
            whereClause.and(card.status.eq(status));
        }
        
        if (minBalance != null && !minBalance.equals(FilterPageCardDTO.DEFAULT_MIN_BALANCE)) {
            whereClause.and(card.balance.goe(minBalance));
        }
        
        if (maxBalance != null && !maxBalance.equals(FilterPageCardDTO.DEFAULT_MAX_BALANCE)) {
            whereClause.and(card.balance.loe(maxBalance));
        }
        
        if (minEndDate != null) {
            whereClause.and(card.validityPeriod.goe(minEndDate));
        }
        
        if (maxEndDate != null) {
            whereClause.and(card.validityPeriod.loe(maxEndDate));
        }
        
        return whereClause;
    }
    
    private long countByCriteria(BooleanBuilder whereClause) {
        return queryFactory
            .select(card.count())
            .from(card)
            .join(card.owner())
            .where(whereClause)
            .fetchOne();
    }
    
    private List<Card> findCardsByCriteria(BooleanBuilder whereClause, Pageable pageable) {
        JPAQuery<Card> query = queryFactory
            .selectFrom(card)
            .join(card.owner()).fetchJoin()
            .where(whereClause);
        
        applySorting(query, pageable.getSort());
        
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        
        return query.fetch();
    }
    
private void applySorting(JPAQuery<Card> query, Sort sort) {
    if (!sort.isSorted()) {
        return;
    }
    
    for (Sort.Order order : sort) {
        String property = order.getProperty();
        boolean ascending = order.isAscending();
        
        OrderSpecifier<?> orderSpecifier = switch (property) {
            case "id" -> ascending ? card.id.asc() : card.id.desc();
            case "cardNumber" -> ascending ? card.cardNumber.asc() : card.cardNumber.desc();
            case "balance" -> ascending ? card.balance.asc() : card.balance.desc();
            case "status" -> ascending ? card.status.asc() : card.status.desc();
            case "validityPeriod" -> ascending ? card.validityPeriod.asc() : card.validityPeriod.desc();
            case "ownerId", "owner.id" -> ascending ? card.owner().id.asc() : card.owner().id.desc();
            case "owner.username" -> ascending ? card.owner().username.asc() : card.owner().username.desc();
            default -> card.id.asc();
        };
        
        query.orderBy(orderSpecifier);
    }
}
}