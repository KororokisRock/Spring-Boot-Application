package com.example.bankcards.controller;

import javax.naming.AuthenticationException;

import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.MessageDTO;
import com.example.bankcards.dto.CardNumberDTO;
import com.example.bankcards.dto.FilterPageCardDTO;
import com.example.bankcards.dto.NewCardDTO;
import com.example.bankcards.dto.PaginatedResponse;
import com.example.bankcards.dto.TransferBetweenCardsDTO;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.ValidateBindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/card")
@Tag(name = "Cards", description = "Bank cards management APIs")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all cards (ADMIN only)", description = "Retrieve paginated list of all cards with filtering")
    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public PaginatedResponse<CardDTO> getAllCards(@RequestBody @Valid FilterPageCardDTO filterPageCardDTO, BindingResult result) {
        PaginatedResponse<CardDTO> response = cardService.getPaginatedAllCardsAsDto(filterPageCardDTO);
        return response;
    }

    @PostMapping("/add")
    @Operation(summary = "Add new card (ADMIN only)", description = "Create a new bank card")
    @ApiResponse(responseCode = "200", description = "Card added successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public MessageDTO addNewCard(@RequestBody @Valid NewCardDTO newCardDTO, BindingResult result) {
        cardService.addNewCard(newCardDTO);
        return new MessageDTO("Card added");
    }

    @PostMapping("/block")
    @Operation(summary = "Block card (ADMIN only)", description = "Block a bank card")
    @ApiResponse(responseCode = "200", description = "Card blocked successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public MessageDTO blockCard(@RequestBody @Valid CardNumberDTO cardNumber, BindingResult result) {
        cardService.blockCard(cardNumber);
        return new MessageDTO("Card blocked");
    }

    @PostMapping("/activate")
    @Operation(summary = "Activate card (ADMIN only)", description = "Activate a bank card")
    @ApiResponse(responseCode = "200", description = "Card activated successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public MessageDTO activeCard(@RequestBody @Valid CardNumberDTO cardNumber, BindingResult result) {
        cardService.activateCard(cardNumber);
        return new MessageDTO("Card activated");
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete card (ADMIN only)", description = "Delete a bank card")
    @ApiResponse(responseCode = "200", description = "Card deleted successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required")
    @ValidateBindingResult
    public MessageDTO deleteCard(@RequestBody @Valid CardNumberDTO cardNumber, BindingResult result) {
        cardService.deleteCard(cardNumber);
        return new MessageDTO("Card deleted");
    }

    @PostMapping("/show")
    @Operation(summary = "Get user's cards", description = "Retrieve paginated list of current user's cards (masked numbers)")
    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully")
    @ValidateBindingResult
    public PaginatedResponse<CardDTO> showCards(Authentication authentication, @RequestBody @Valid FilterPageCardDTO filterPageCardDTO, BindingResult result) {
        PaginatedResponse<CardDTO> response = cardService.getPaginatedAllUserCardsAsDto(filterPageCardDTO, authentication.getName(), true);
        return response;
    }

    @PostMapping("/show-full-number")
    @Operation(summary = "Get user's cards with full numbers", description = "Retrieve paginated list of current user's cards with full card numbers")
    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully")
    @ValidateBindingResult
    public PaginatedResponse<CardDTO> showFullNumberCards(Authentication authentication, @RequestBody @Valid FilterPageCardDTO filters) throws AuthenticationException {
        PaginatedResponse<CardDTO> response = cardService.getPaginatedAllUserCardsAsDto(filters, authentication.getName(), false);
        return response;
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer between cards", description = "Transfer money between user's own cards")
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully")
    @ApiResponse(responseCode = "400", description = "Validation error or insufficient funds")
    @ValidateBindingResult
    public MessageDTO transferCards(Authentication authentication, @RequestBody @Valid TransferBetweenCardsDTO transferBetweenCardsDTO, BindingResult result) {
        cardService.transferBetweenCards(authentication, transferBetweenCardsDTO);
        return new MessageDTO("Transfer between cards completed successfully");
    }
}
