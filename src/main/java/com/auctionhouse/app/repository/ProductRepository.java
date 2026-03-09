package com.auctionhouse.app.repository;

import com.auctionhouse.app.model.Product;
import com.auctionhouse.app.model.ProductStatus;
import com.auctionhouse.app.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);
    List<Product> findBySellerOrderByCreatedAtDesc(User seller);
    long countByStatus(ProductStatus status);
}
