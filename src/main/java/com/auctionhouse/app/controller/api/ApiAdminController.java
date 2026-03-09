package com.auctionhouse.app.controller.api;

import com.auctionhouse.app.dto.api.ApiAdminDashboardResponse;
import com.auctionhouse.app.dto.api.ApiAuctionView;
import com.auctionhouse.app.dto.api.ApiMessageResponse;
import com.auctionhouse.app.service.AdminService;
import com.auctionhouse.app.service.ApiMapperService;
import com.auctionhouse.app.service.AuctionService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class ApiAdminController {

    private final AdminService adminService;
    private final AuctionService auctionService;
    private final ApiMapperService mapper;

    public ApiAdminController(AdminService adminService, AuctionService auctionService, ApiMapperService mapper) {
        this.adminService = adminService;
        this.auctionService = auctionService;
        this.mapper = mapper;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiAdminDashboardResponse> dashboard() {
        ApiAuctionView current = auctionService.getCurrentAuction().map(mapper::toAuctionView).orElse(null);
        List<ApiAuctionView> upcoming = auctionService.getAllUpcomingAuctions().stream().map(mapper::toAuctionView).toList();

        ApiAdminDashboardResponse response = new ApiAdminDashboardResponse(
                adminService.getDashboardMetrics(),
                current,
                upcoming,
                adminService.getPendingProducts().stream().map(mapper::toApprovalView).toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/products/{id}/approve")
    public ResponseEntity<ApiMessageResponse> approve(@PathVariable Long id) {
        adminService.approveProduct(id);
        return ResponseEntity.ok(new ApiMessageResponse("Product approved and auction scheduled"));
    }

    @PostMapping("/products/{id}/reject")
    public ResponseEntity<ApiMessageResponse> reject(@PathVariable Long id) {
        adminService.rejectProduct(id);
        return ResponseEntity.ok(new ApiMessageResponse("Product rejected"));
    }
}
