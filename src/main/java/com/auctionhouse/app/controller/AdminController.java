package com.auctionhouse.app.controller;

import com.auctionhouse.app.service.AdminService;
import com.auctionhouse.app.service.AuctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final AuctionService auctionService;

    public AdminController(AdminService adminService, AuctionService auctionService) {
        this.adminService = adminService;
        this.auctionService = auctionService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("metrics", adminService.getDashboardMetrics());
        model.addAttribute("pendingProducts", adminService.getPendingProducts());
        model.addAttribute("currentAuction", auctionService.getCurrentAuction().orElse(null));
        model.addAttribute("upcomingAuctions", auctionService.getAllUpcomingAuctions());
        return "admin-dashboard";
    }

    @PostMapping("/admin/products/{id}/approve")
    public String approveProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.approveProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product approved and moved to upcoming auctions.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/products/{id}/reject")
    public String rejectProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.rejectProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product rejected.");
        return "redirect:/admin/dashboard";
    }
}
