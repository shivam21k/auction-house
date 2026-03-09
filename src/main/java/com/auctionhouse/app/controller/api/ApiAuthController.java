package com.auctionhouse.app.controller.api;

import com.auctionhouse.app.dto.SignupRequest;
import com.auctionhouse.app.dto.api.ApiAuthResponse;
import com.auctionhouse.app.dto.api.LoginRequest;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.security.jwt.JwtService;
import com.auctionhouse.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public ApiAuthController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiAuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        userService.registerUser(request);
        User user = userService.getByEmail(request.getEmail());
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiAuthResponse(token, user.getRole().name(), user.getEmail(), user.getFullName()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userService.getByEmail(normalizedEmail);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new ApiAuthResponse(token, user.getRole().name(), user.getEmail(), user.getFullName()));
    }
}
