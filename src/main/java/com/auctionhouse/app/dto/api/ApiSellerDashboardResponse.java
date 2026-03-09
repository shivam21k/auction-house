package com.auctionhouse.app.dto.api;

import java.util.List;

public record ApiSellerDashboardResponse(ApiSellerProfileResponse seller, List<ApiProductView> products) {
}
