package com.example.bankcards.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardNumberDTO;
import com.example.bankcards.dto.FilterPageCardDTO;
import com.example.bankcards.dto.NewCardDTO;
import com.example.bankcards.dto.PaginatedResponse;
import com.example.bankcards.dto.TransferBetweenCardsDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.STATUS;
import com.example.bankcards.exception.ActivatedAlreadyException;
import com.example.bankcards.exception.BlockedAlreadyException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.NotEnoughBalanceException;
import com.example.bankcards.exception.NotYourCardException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class CardService {
    private final CardRepository cardRepo;

    private final UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    public CardService(CardRepository cardRepo, UserRepository userRepo) {
        this.cardRepo = cardRepo;
        this.userRepo = userRepo;
    }

    public void transferBetweenCards(Authentication authentication, TransferBetweenCardsDTO transfer) {
        String username = authentication.getName();

        Optional<Integer> optionalId = userRepo.findUserIdByUsername(username);
        if (optionalId.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        Integer userId = optionalId.get();

        Optional<Card> optionalFirstCard = cardRepo.findByCardNumber(transfer.getFirstCardNumber());
        if (optionalFirstCard.isEmpty()) {
            throw new CardNotFoundException(transfer.getFirstCardNumber());
        }
        Card firstCard = optionalFirstCard.get();

        if (!Objects.equals(firstCard.getOwnerId(), userId)) {
            throw new NotYourCardException();
        }

        if (firstCard.getBalance() < transfer.getAmountTransferBetweenCards()) {
            throw new NotEnoughBalanceException();
        }

        Optional<Card> optionalSecondCard = cardRepo.findByCardNumber(transfer.getSecondCardNumber());
        if (optionalSecondCard.isEmpty()) {
            throw new CardNotFoundException(transfer.getSecondCardNumber());
        }
        Card secondCard = optionalSecondCard.get();

        if (!Objects.equals(secondCard.getOwnerId(), userId)) {
            throw new NotYourCardException();
        }

        firstCard.setBalance(firstCard.getBalance() - transfer.getAmountTransferBetweenCards());
        secondCard.setBalance(secondCard.getBalance() + transfer.getAmountTransferBetweenCards());

        cardRepo.save(firstCard);
        cardRepo.save(secondCard);
    }

    public void addNewCard(NewCardDTO newCard) {
        Card card = new Card();
        card.setCardNumber(newCard.getCardNumber());
        card.setOwnerId(newCard.getOwnerId());
        card.setValidityPeriod(newCard.getValidityPeriod());
        card.setBalance(newCard.getBalance());
        card.setStatus(STATUS.ACTIVE);
        cardRepo.save(card);
    }

    public void deleteCard(CardNumberDTO cardNumber) {
        cardRepo.deleteByCardNumber(cardNumber.getCardNumber());
    }

    public void blockCard(CardNumberDTO cardNumber) {
        Optional<Card> optionalCard = cardRepo.findByCardNumber(cardNumber.getCardNumber());
        if (optionalCard.isEmpty()) {
            throw new CardNotFoundException(cardNumber.getCardNumber());
        }
        Card card = optionalCard.get();

        if (card.getStatus() == STATUS.BLOCKED) {
            throw new BlockedAlreadyException(card.getCardNumber());
        }
        card.setStatus(STATUS.BLOCKED);
        cardRepo.save(card);
    }

    public void activateCard(CardNumberDTO cardNumber) {
        Optional<Card> optionalCard = cardRepo.findByCardNumber(cardNumber.getCardNumber());
        if (optionalCard.isEmpty()) {
            throw new CardNotFoundException(cardNumber.getCardNumber());
        }
        Card card = optionalCard.get();

        if (card.getStatus() == STATUS.ACTIVE) {
            throw new ActivatedAlreadyException(card.getCardNumber());
        }
        card.setStatus(STATUS.ACTIVE);
        cardRepo.save(card);
    }

    public PaginatedResponse<CardDTO> getPaginatedAllCardsAsDto(FilterPageCardDTO filters) {
        logger.info("FilterPageCardDTO: Username[ {} ] Direction sort[ {} ] Sort by:[ {} ] Page number:[ {} ] Page size:[ {} ] CardNumber:[ {} ] MinEndDate:[ {} ] MaxEndDate:[ {} ] Status: [ {} ] MinBalance:[ {} ] MaxBalance:[ {} ]",
        filters.getUsername(), filters.getDirectionSort(), filters.getSortBy(),
        filters.getPage(), filters.getSize(), filters.getCardNumber(), filters.getMinEndDate(), filters.getMaxEndDate(),
        filters.getStatus(), filters.getMinBalance(), filters.getMaxBalance());
        Page<Card> cardsPage = cardRepo.findByCriteria(
            filters.getUsername(),
            filters.getCardNumber(),
            filters.getStatus(),
            filters.getMinBalance(),
            filters.getMaxBalance(),
            filters.getMinEndDate(),
            filters.getMaxEndDate(),
            true,
            getPageableForCard(filters.getPage(), filters.getSize(), getSortForCard(filters.getDirectionSort(), filters.getSortBy()))
        );

        PaginatedResponse<CardDTO> response = new PaginatedResponse<>(
            fromPageToList(cardsPage, true),
            cardsPage.getNumber(),
            cardsPage.getTotalPages(),
            cardsPage.getTotalElements(),
            cardsPage.getSize(),
            cardsPage.hasNext(),
            cardsPage.hasPrevious()
        );
        return response;
    }

    public PaginatedResponse<CardDTO> getPaginatedAllUserCardsAsDto(FilterPageCardDTO filters, String username, boolean maskCardNumber) {
        Page<Card> cardsPage = cardRepo.findByCriteria(
            username,
            filters.getCardNumber(),
            filters.getStatus(),
            filters.getMinBalance(),
            filters.getMaxBalance(),
            filters.getMinEndDate(),
            filters.getMaxEndDate(),
            false,
            getPageableForCard(filters.getPage(), filters.getSize(), getSortForCard(filters.getDirectionSort(), filters.getSortBy()))
        );
        PaginatedResponse<CardDTO> response = new PaginatedResponse<>(
            fromPageToList(cardsPage, maskCardNumber),
            cardsPage.getNumber(),
            cardsPage.getTotalPages(),
            cardsPage.getTotalElements(),
            cardsPage.getSize(),
            cardsPage.hasNext(),
            cardsPage.hasPrevious()
        );
        return response;
    }

    private Sort getSortForCard(String directionSort, String sortBy) {
        Sort sort = directionSort.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        return sort;
    }

    private List<CardDTO> fromPageToList(Page<Card> cardsPage, boolean maskCardNumber) {
        return cardsPage.getContent().stream()
        .map(card -> {
            return maskCardNumber ? CardDTO.newCardDTOWithMaksedNumber(card) : CardDTO.newCardDTOWithFullNumber(card);
        })
        .collect(Collectors.toList());
    }

    private Pageable getPageableForCard(int pageNumber, int pageSize, Sort sort) {
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
