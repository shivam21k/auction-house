package com.auctionhouse.app.dto.api;

public record ApiAuthResponse(String token, String role, String email, String fullName) {
}
