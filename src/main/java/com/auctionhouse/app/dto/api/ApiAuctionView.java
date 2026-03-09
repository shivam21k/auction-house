package com.auctionhouse.app.dto.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApiAuctionView(
        Long auctionId,
        String title,
        String description,
        String category,
        BigDecimal basePrice,
        BigDecimal currentBid,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String sellerName,
        String status
) {
}
