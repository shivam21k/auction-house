package com.auctionhouse.app.controller.api;

import com.auctionhouse.app.dto.BidRequest;
import com.auctionhouse.app.dto.api.ApiBuyerDashboardResponse;
import com.auctionhouse.app.dto.api.ApiMessageResponse;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.service.ApiMapperService;
import com.auctionhouse.app.service.AuctionService;
import com.auctionhouse.app.service.BuyerService;
import com.auctionhouse.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/buyer")
public class ApiBuyerController {

    private final AuctionService auctionService;
    private final BuyerService buyerService;
    private final UserService userService;
    private final ApiMapperService mapper;

    public ApiBuyerController(
            AuctionService auctionService,
            BuyerService buyerService,
            UserService userService,
            ApiMapperService mapper) {
        this.auctionService = auctionService;
        this.buyerService = buyerService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiBuyerDashboardResponse> dashboard() {
        return ResponseEntity.ok(new ApiBuyerDashboardResponse(
                auctionService.getCurrentAuction().map(mapper::toAuctionView).orElse(null),
                auctionService.getUpcomingAuctions().stream().map(mapper::toAuctionView).toList()));
    }

    @PostMapping("/bid")
    public ResponseEntity<ApiMessageResponse> placeBid(Authentication authentication, @Valid @RequestBody BidRequest request) {
        User buyer = userService.getByEmail(authentication.getName());
        buyerService.placeBid(request.getAuctionId(), request.getAmount(), buyer);
        return ResponseEntity.ok(new ApiMessageResponse("Bid placed successfully"));
    }
}
