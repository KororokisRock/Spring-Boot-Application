package com.example.bankcards.dto;

import java.time.LocalDate;

import com.example.bankcards.entity.STATUS;
import com.example.bankcards.util.FilterPageCardValid;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filters and pagination for card search")
@FilterPageCardValid
public class FilterPageCardDTO {
    
    public static final Double DEFAULT_MIN_BALANCE = -1.7976931348623157E+308;
    public static final Double DEFAULT_MAX_BALANCE = 1.7976931348623157E308;

    @Schema(description = "Username filter", example = "Kororok")
    private String username = null;
    @Schema(description = "Sort direction", example = "asc", allowableValues = {"asc", "desc"})
    private String directionSort = "asc";
    @Schema(description = "Sort by field", example = "id", allowableValues = {"id", "card_number", "owner_id", "validity_period", "status", "balance"})
    private String sortBy = "id";
    @Schema(description = "Page number (0-based)", example = "0")
    private int page = 0;
    @Schema(description = "Page size", example = "10")
    private int size = 10;
    @Schema(description = "Card number filter (partial match)", example = "1234")
    private String cardNumber = null;
    @Schema(description = "Minimum expiration date", example = "2024-01-01")
    private LocalDate minEndDate = null;
    @Schema(description = "Maximum expiration date", example = "2025-12-31")
    private LocalDate maxEndDate = null;
    @Schema(description = "Card status filter", example = "ACTIVE")
    private STATUS status = null;
    @Schema(description = "Minimum balance", example = "0.0")
    private Double minBalance = DEFAULT_MIN_BALANCE;
    @Schema(description = "Maximum balance", example = "10000.0")
    private Double maxBalance = DEFAULT_MAX_BALANCE;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDirectionSort() {
        return directionSort;
    }

    public void setDirectionSort(String directionSort) {
        this.directionSort = directionSort;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getMinEndDate() {
        return minEndDate;
    }
    public void setMinEndDate(LocalDate minEndDate)  {
        this.minEndDate = minEndDate;
    }

    public LocalDate getMaxEndDate() {
        return maxEndDate;
    }
    public void setMaxEndDate(LocalDate maxEndDate)  {
        this.maxEndDate = maxEndDate;
    }

    public STATUS getStatus() {
        return status;
    }
    public void setStatus(STATUS status)  {
        this.status = status;
    }

    public double getMinBalance() {
        return minBalance;
    }
    public void setMinBalance(double minBalance)  {
        this.minBalance = minBalance;
    }

    public double getMaxBalance() {
        return maxBalance;
    }
    public void setMaxBalance(double maxBalance)  {
        this.maxBalance = maxBalance;
    }
}
