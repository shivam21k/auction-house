package com.auctionhouse.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BidRequest {

    @NotNull
    private Long auctionId;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "1.0", message = "Bid should be greater than zero")
    private BigDecimal amount;

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
