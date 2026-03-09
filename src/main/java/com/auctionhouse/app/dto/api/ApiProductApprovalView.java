package com.auctionhouse.app.dto.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApiProductApprovalView(
        Long productId,
        String title,
        BigDecimal basePrice,
        LocalDateTime requestedSlotStart,
        LocalDateTime requestedSlotEnd,
        String sellerName,
        String sellerEmail
) {
}
