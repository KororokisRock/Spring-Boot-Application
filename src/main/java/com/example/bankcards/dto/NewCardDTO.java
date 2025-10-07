package com.example.bankcards.dto;

import java.time.LocalDate;

import com.example.bankcards.util.NoOneHasNumber;
import com.example.bankcards.util.UserWithIdExist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Data for creating a new card")
public class NewCardDTO {
    
    @Schema(description = "16-digit card number", example = "1234567812345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="Cardnumber is required")
    @Pattern(regexp="^[0-9]{16}$", message="Card number must be exactly 16 digits")
    @NoOneHasNumber
    private String cardNumber;

    @Schema(description = "Owner user ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message="Owner id is required")
    @Positive
    @UserWithIdExist
    private Integer ownerId;

    @Schema(description = "Card expiration date", example = "2025-12-31", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message="Validity period is required")
    @Future
    private LocalDate validityPeriod;

    @Schema(description = "Initial balance", example = "1000.0", defaultValue = "0.0")
    @PositiveOrZero
    private double balance = 0.0;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(LocalDate validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
