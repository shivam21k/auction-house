package com.auctionhouse.app.dto.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApiProductView(
        Long productId,
        String title,
        String category,
        BigDecimal basePrice,
        BigDecimal reservePrice,
        String status,
        LocalDateTime requestedSlotStart,
        LocalDateTime requestedSlotEnd
) {
}
