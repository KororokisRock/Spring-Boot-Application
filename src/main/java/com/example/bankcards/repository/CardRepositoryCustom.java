package com.example.bankcards.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.STATUS;

public interface CardRepositoryCustom {
    Page<Card> findByCriteria(
        String username,
        String cardNumber,
        STATUS status,
        Double minBalance,
        Double maxBalance,
        LocalDate minEndDate,
        LocalDate maxEndDate,
        Boolean isAdminSearch,
        Pageable pageable
    );
}