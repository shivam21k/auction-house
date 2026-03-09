package com.auctionhouse.app.dto.api;

public record ApiSellerProfileResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String address,
        String companyName
) {
}
