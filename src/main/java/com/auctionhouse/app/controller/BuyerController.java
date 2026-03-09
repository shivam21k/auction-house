package com.auctionhouse.app.controller;

import com.auctionhouse.app.dto.BidRequest;
import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.service.AuctionService;
import com.auctionhouse.app.service.BuyerService;
import com.auctionhouse.app.service.UserService;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BuyerController {

    private final AuctionService auctionService;
    private final BuyerService buyerService;
    private final UserService userService;

    public BuyerController(AuctionService auctionService, BuyerService buyerService, UserService userService) {
        this.auctionService = auctionService;
        this.buyerService = buyerService;
        this.userService = userService;
    }

    @GetMapping("/buyer/dashboard")
    public String buyerDashboard(Model model) {
        populateDashboardModel(model, new BidRequest());
        return "buyer-dashboard";
    }

    @PostMapping("/buyer/bid")
    public String placeBid(
            Authentication authentication,
            @Valid @ModelAttribute("bidRequest") BidRequest bidRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            populateDashboardModel(model, bidRequest);
            return "buyer-dashboard";
        }

        try {
            User buyer = userService.getByEmail(authentication.getName());
            buyerService.placeBid(bidRequest.getAuctionId(), bidRequest.getAmount(), buyer);
            redirectAttributes.addFlashAttribute("successMessage", "Bid placed successfully.");
            return "redirect:/buyer/dashboard";
        } catch (IllegalArgumentException ex) {
            populateDashboardModel(model, bidRequest);
            model.addAttribute("errorMessage", ex.getMessage());
            return "buyer-dashboard";
        }
    }

    @GetMapping("/buyer/live-auction")
    @ResponseBody
    public Map<String, Object> liveAuctionSnapshot() {
        Map<String, Object> payload = new LinkedHashMap<>();
        Optional<Auction> currentAuction = auctionService.getCurrentAuction();

        payload.put("live", currentAuction.isPresent());
        if (currentAuction.isEmpty()) {
            return payload;
        }

        Auction auction = currentAuction.get();
        long secondsLeft = Math.max(0, Duration.between(LocalDateTime.now(), auction.getEndTime()).toSeconds());

        payload.put("auctionId", auction.getId());
        payload.put("productTitle", auction.getProduct().getTitle());
        payload.put("currentBid", auction.getCurrentBid());
        payload.put("secondsLeft", secondsLeft);
        payload.put("endTime", auction.getEndTime().toString());
        return payload;
    }

    private void populateDashboardModel(Model model, BidRequest bidRequest) {
        Optional<Auction> currentAuction = auctionService.getCurrentAuction();
        if (currentAuction.isPresent() && bidRequest.getAuctionId() == null) {
            bidRequest.setAuctionId(currentAuction.get().getId());
        }
        model.addAttribute("currentAuction", currentAuction.orElse(null));
        model.addAttribute("upcomingAuctions", auctionService.getUpcomingAuctions());
        model.addAttribute("awaitingApprovalAuctions", auctionService.getAwaitingAdminApprovalAuctions());
        model.addAttribute("bidRequest", bidRequest);
    }
}
