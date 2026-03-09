package com.auctionhouse.app.dto.api;

import java.util.List;
import java.util.Map;

public record ApiAdminDashboardResponse(
        Map<String, Long> metrics,
        ApiAuctionView currentAuction,
        List<ApiAuctionView> upcomingAuctions,
        List<ApiProductApprovalView> pendingApprovals
) {
}
