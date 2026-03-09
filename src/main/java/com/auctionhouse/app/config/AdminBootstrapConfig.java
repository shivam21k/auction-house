package com.auctionhouse.app.config;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.AuctionStatus;
import com.auctionhouse.app.model.Bid;
import com.auctionhouse.app.model.Product;
import com.auctionhouse.app.model.ProductStatus;
import com.auctionhouse.app.model.Role;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.repository.AuctionRepository;
import com.auctionhouse.app.repository.BidRepository;
import com.auctionhouse.app.repository.ProductRepository;
import com.auctionhouse.app.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            ProductRepository productRepository,
            AuctionRepository auctionRepository,
            BidRepository bidRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name}") String adminName,
            @Value("${app.admin.email}") String adminEmail,
            @Value("${app.admin.password}") String adminPassword,
            @Value("${app.demo.seed:true}") boolean seedDemoData) {
        return args -> {
            ensureAdmin(userRepository, passwordEncoder, adminName, adminEmail, adminPassword);

            if (!seedDemoData) {
                return;
            }

            List<User> demoUsers = ensureDemoUsers(userRepository, passwordEncoder);
            boolean catalogMissing = productRepository.count() == 0 && auctionRepository.count() == 0;
            if (catalogMissing) {
                seedDemoCatalog(productRepository, auctionRepository, bidRepository, demoUsers);
            }
        };
    }

    private void ensureAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String adminName,
            String adminEmail,
            String adminPassword) {
        if (userRepository.existsByEmail(adminEmail.toLowerCase().trim())) {
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
    }

    private List<User> ensureDemoUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode("Password@123");

        User seller1 = ensureUser(userRepository, "Ananya Rao", "seller1@auctionhouse.com", Role.SELLER,
                encodedPassword, "+91-9000000001", "Mumbai", "Rao Antiques");
        User seller2 = ensureUser(userRepository, "Kunal Mehta", "seller2@auctionhouse.com", Role.SELLER,
                encodedPassword, "+91-9000000002", "Delhi", "Mehta Heritage");
        User seller3 = ensureUser(userRepository, "Isha Sen", "seller3@auctionhouse.com", Role.SELLER,
                encodedPassword, "+91-9000000003", "Kolkata", "Sen Vintage Arts");

        User buyer1 = ensureUser(userRepository, "Rohit Verma", "buyer1@auctionhouse.com", Role.BUYER,
                encodedPassword, "+91-9000000011", "Pune", "");
        User buyer2 = ensureUser(userRepository, "Neha Kapoor", "buyer2@auctionhouse.com", Role.BUYER,
                encodedPassword, "+91-9000000012", "Bengaluru", "");
        User buyer3 = ensureUser(userRepository, "Arjun Das", "buyer3@auctionhouse.com", Role.BUYER,
                encodedPassword, "+91-9000000013", "Hyderabad", "");

        return List.of(seller1, seller2, seller3, buyer1, buyer2, buyer3);
    }

    private User ensureUser(
            UserRepository userRepository,
            String fullName,
            String email,
            Role role,
            String encodedPassword,
            String phone,
            String address,
            String companyName) {
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseGet(() -> {
                    User user = new User();
                    user.setFullName(fullName);
                    user.setEmail(email.toLowerCase().trim());
                    user.setPassword(encodedPassword);
                    user.setRole(role);
                    user.setPhone(phone);
                    user.setAddress(address);
                    user.setCompanyName(companyName);
                    return userRepository.save(user);
                });
    }

    private void seedDemoCatalog(
            ProductRepository productRepository,
            AuctionRepository auctionRepository,
            BidRepository bidRepository,
            List<User> users) {
        User seller1 = users.get(0);
        User seller2 = users.get(1);
        User seller3 = users.get(2);
        User buyer1 = users.get(3);
        User buyer2 = users.get(4);

        LocalDateTime now = LocalDateTime.now();
        List<Product> products = new ArrayList<>();

        Product liveProduct = buildProduct(
                seller1,
                "Mughal Era Silver Dagger",
                "Curated 18th century ceremonial dagger with carved ivory grip.",
                "Weapons & Artifacts",
                new BigDecimal("75000"),
                new BigDecimal("90000"),
                now.minusSeconds(20),
                now.plusMinutes(1),
                ProductStatus.LIVE);
        products.add(liveProduct);

        Product upcomingProduct1 = buildProduct(
                seller2,
                "Victorian Oil Painting",
                "Original framed canvas from private British estate collection.",
                "Fine Art",
                new BigDecimal("120000"),
                new BigDecimal("150000"),
                now.plusHours(2),
                now.plusHours(3),
                ProductStatus.UPCOMING);
        products.add(upcomingProduct1);

        Product upcomingProduct2 = buildProduct(
                seller3,
                "Bronze Chola Replica",
                "Museum-grade handcrafted bronze replica with certificate.",
                "Sculptures",
                new BigDecimal("45000"),
                new BigDecimal("55000"),
                now.plusHours(4),
                now.plusHours(5),
                ProductStatus.UPCOMING);
        products.add(upcomingProduct2);

        Product pendingProduct1 = buildProduct(
                seller1,
                "Persian Silk Carpet",
                "Hand-knotted silk carpet in preserved condition.",
                "Textiles",
                new BigDecimal("38000"),
                new BigDecimal("50000"),
                now.plusHours(8),
                now.plusHours(9),
                ProductStatus.PENDING_APPROVAL);
        products.add(pendingProduct1);

        Product pendingProduct2 = buildProduct(
                seller2,
                "Colonial Brass Telescope",
                "Working nautical telescope with original casing.",
                "Instruments",
                new BigDecimal("26000"),
                new BigDecimal("32000"),
                now.plusHours(10),
                now.plusHours(11),
                ProductStatus.PENDING_APPROVAL);
        products.add(pendingProduct2);

        productRepository.saveAll(products);

        Auction liveAuction = buildAuction(liveProduct, liveProduct.getRequestedSlotStart(), liveProduct.getRequestedSlotEnd(),
                new BigDecimal("91000"), AuctionStatus.LIVE);
        liveAuction.setWinner(buyer2);

        Auction upcomingAuction1 = buildAuction(upcomingProduct1, upcomingProduct1.getRequestedSlotStart(),
                upcomingProduct1.getRequestedSlotEnd(), upcomingProduct1.getBasePrice(), AuctionStatus.UPCOMING);
        Auction upcomingAuction2 = buildAuction(upcomingProduct2, upcomingProduct2.getRequestedSlotStart(),
                upcomingProduct2.getRequestedSlotEnd(), upcomingProduct2.getBasePrice(), AuctionStatus.UPCOMING);
        auctionRepository.saveAll(List.of(liveAuction, upcomingAuction1, upcomingAuction2));

        Bid bid1 = new Bid();
        bid1.setAuction(liveAuction);
        bid1.setBuyer(buyer1);
        bid1.setAmount(new BigDecimal("82000"));
        bid1.setBidTime(now.minusMinutes(15));

        Bid bid2 = new Bid();
        bid2.setAuction(liveAuction);
        bid2.setBuyer(buyer2);
        bid2.setAmount(new BigDecimal("91000"));
        bid2.setBidTime(now.minusMinutes(4));

        bidRepository.saveAll(List.of(bid1, bid2));
    }

    private Product buildProduct(
            User seller,
            String title,
            String description,
            String category,
            BigDecimal basePrice,
            BigDecimal reservePrice,
            LocalDateTime start,
            LocalDateTime end,
            ProductStatus status) {
        Product product = new Product();
        product.setSeller(seller);
        product.setTitle(title);
        product.setDescription(description);
        product.setCategory(category);
        product.setBasePrice(basePrice);
        product.setReservePrice(reservePrice);
        product.setRequestedSlotStart(start);
        product.setRequestedSlotEnd(end);
        product.setStatus(status);
        return product;
    }

    private Auction buildAuction(
            Product product,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal currentBid,
            AuctionStatus status) {
        Auction auction = new Auction();
        auction.setProduct(product);
        auction.setStartTime(startTime);
        auction.setEndTime(endTime);
        auction.setCurrentBid(currentBid);
        auction.setStatus(status);
        return auction;
    }
}


