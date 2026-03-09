package com.auctionhouse.app.controller;

import com.auctionhouse.app.dto.SignupRequest;
import com.auctionhouse.app.model.Role;
import com.auctionhouse.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        model.addAttribute("roles", new Role[]{Role.BUYER, Role.SELLER});
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", new Role[]{Role.BUYER, Role.SELLER});
            return "signup";
        }

        try {
            userService.registerUser(signupRequest);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("roles", new Role[]{Role.BUYER, Role.SELLER});
            model.addAttribute("errorMessage", ex.getMessage());
            return "signup";
        }

        return "redirect:/login?registered";
    }
}
