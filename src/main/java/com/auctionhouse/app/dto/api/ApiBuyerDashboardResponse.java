package com.auctionhouse.app.dto.api;

import java.util.List;

public record ApiBuyerDashboardResponse(ApiAuctionView currentAuction, List<ApiAuctionView> upcomingAuctions) {
}
