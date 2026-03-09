package com.auctionhouse.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class ProductRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String category;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "1.0", message = "Base price must be greater than zero")
    private BigDecimal basePrice;

    @DecimalMin(value = "1.0", message = "Reserve price must be greater than zero")
    private BigDecimal reservePrice;

    @NotNull(message = "Start slot is required")
    @Future(message = "Start slot must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime requestedSlotStart;

    @NotNull(message = "End slot is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime requestedSlotEnd;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    public LocalDateTime getRequestedSlotStart() {
        return requestedSlotStart;
    }

    public void setRequestedSlotStart(LocalDateTime requestedSlotStart) {
        this.requestedSlotStart = requestedSlotStart;
    }

    public LocalDateTime getRequestedSlotEnd() {
        return requestedSlotEnd;
    }

    public void setRequestedSlotEnd(LocalDateTime requestedSlotEnd) {
        this.requestedSlotEnd = requestedSlotEnd;
    }
}
