package com.auctionhouse.app.controller;

import com.auctionhouse.app.dto.ProductRequest;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.service.SellerService;
import com.auctionhouse.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SellerController {

    private final UserService userService;
    private final SellerService sellerService;

    public SellerController(UserService userService, SellerService sellerService) {
        this.userService = userService;
        this.sellerService = sellerService;
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(Authentication authentication, Model model) {
        User seller = userService.getByEmail(authentication.getName());
        model.addAttribute("seller", seller);
        model.addAttribute("products", sellerService.getSellerProducts(seller));
        model.addAttribute("productRequest", new ProductRequest());
        return "seller-dashboard";
    }

    @PostMapping("/seller/products")
    public String addProduct(
            Authentication authentication,
            @Valid @ModelAttribute("productRequest") ProductRequest productRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        User seller = userService.getByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("seller", seller);
            model.addAttribute("products", sellerService.getSellerProducts(seller));
            return "seller-dashboard";
        }

        try {
            sellerService.createProduct(seller, productRequest);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("seller", seller);
            model.addAttribute("products", sellerService.getSellerProducts(seller));
            model.addAttribute("errorMessage", ex.getMessage());
            return "seller-dashboard";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Product submitted for admin approval.");
        return "redirect:/seller/dashboard";
    }

    @GetMapping("/seller/profile")
    public String sellerProfile(Authentication authentication, Model model) {
        model.addAttribute("seller", userService.getByEmail(authentication.getName()));
        return "seller-profile";
    }
}
