package com.auctionhouse.app.config;

import com.auctionhouse.app.model.Role;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    public CommandLineRunner adminBootstrapRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name}") String adminName,
            @Value("${app.admin.email}") String adminEmail,
            @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = new User();
            admin.setFullName(adminName);
            admin.setEmail(adminEmail.toLowerCase().trim());
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setPhone("N/A");
            admin.setAddress("System");
            admin.setCompanyName("AuctionHouse");

            userRepository.save(admin);
        };
    }
}
