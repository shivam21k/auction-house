package com.auctionhouse.app.service;

import com.auctionhouse.app.dto.SignupRequest;
import com.auctionhouse.app.model.Role;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignupRequest signupRequest) {
        if (signupRequest.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Admin registration is not allowed");
        }

        String normalizedEmail = signupRequest.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(signupRequest.getRole());
        user.setPhone(signupRequest.getPhone());
        user.setAddress(signupRequest.getAddress());
        user.setCompanyName(signupRequest.getCompanyName());

        userRepository.save(user);
    }

    public User getByEmail(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        return userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
