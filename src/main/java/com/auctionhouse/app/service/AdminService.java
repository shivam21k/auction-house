package com.auctionhouse.app.service;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.AuctionStatus;
import com.auctionhouse.app.model.Product;
import com.auctionhouse.app.model.ProductStatus;
import com.auctionhouse.app.model.Role;
import com.auctionhouse.app.repository.AuctionRepository;
import com.auctionhouse.app.repository.ProductRepository;
import com.auctionhouse.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public AdminService(
            ProductRepository productRepository,
            AuctionRepository auctionRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Long> getDashboardMetrics() {
        Map<String, Long> metrics = new LinkedHashMap<>();
        metrics.put("buyers", userRepository.countByRole(Role.BUYER));
        metrics.put("sellers", userRepository.countByRole(Role.SELLER));
        metrics.put("productsPending", productRepository.countByStatus(ProductStatus.PENDING_APPROVAL));
        metrics.put("productsApproved", productRepository.countByStatus(ProductStatus.APPROVED));
        metrics.put("auctionsUpcoming", auctionRepository.countByStatus(AuctionStatus.UPCOMING));
        metrics.put("auctionsLive", auctionRepository.countByStatus(AuctionStatus.LIVE));
        return metrics;
    }

    public List<Product> getPendingProducts() {
        return productRepository.findByStatusOrderByCreatedAtDesc(ProductStatus.PENDING_APPROVAL);
    }

    @Transactional
    public void approveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setStatus(ProductStatus.UPCOMING);

        Auction auction = new Auction();
        auction.setProduct(product);
        auction.setStartTime(product.getRequestedSlotStart());
        auction.setEndTime(product.getRequestedSlotEnd());
        auction.setCurrentBid(product.getBasePrice());
        auction.setStatus(AuctionStatus.UPCOMING);

        productRepository.save(product);
        auctionRepository.save(auction);
    }

    @Transactional
    public void rejectProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setStatus(ProductStatus.REJECTED);
        productRepository.save(product);
    }
}
