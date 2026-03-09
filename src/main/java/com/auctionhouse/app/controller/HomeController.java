package com.auctionhouse.app.controller;

import com.auctionhouse.app.model.Role;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return "index";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboardRedirect(Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        if (user.getRole() == Role.SELLER) {
            return "redirect:/seller/dashboard";
        }
        return "redirect:/buyer/dashboard";
    }
}
