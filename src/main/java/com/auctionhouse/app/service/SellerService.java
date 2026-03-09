package com.auctionhouse.app.service;

import com.auctionhouse.app.dto.ProductRequest;
import com.auctionhouse.app.model.Product;
import com.auctionhouse.app.model.ProductStatus;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private final ProductRepository productRepository;

    public SellerService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getSellerProducts(User seller) {
        return productRepository.findBySellerOrderByCreatedAtDesc(seller);
    }

    @Transactional
    public void createProduct(User seller, ProductRequest request) {
        if (request.getRequestedSlotEnd().isBefore(request.getRequestedSlotStart())
                || request.getRequestedSlotEnd().isEqual(request.getRequestedSlotStart())) {
            throw new IllegalArgumentException("Slot end time must be after start time");
        }

        Product product = new Product();
        product.setSeller(seller);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBasePrice(request.getBasePrice());
        product.setReservePrice(request.getReservePrice());
        product.setRequestedSlotStart(request.getRequestedSlotStart());
        product.setRequestedSlotEnd(request.getRequestedSlotEnd());
        product.setStatus(ProductStatus.PENDING_APPROVAL);

        productRepository.save(product);
    }
}
