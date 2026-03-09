package com.auctionhouse.app.controller.api;

import com.auctionhouse.app.dto.ProductRequest;
import com.auctionhouse.app.dto.api.ApiMessageResponse;
import com.auctionhouse.app.dto.api.ApiSellerDashboardResponse;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.service.ApiMapperService;
import com.auctionhouse.app.service.SellerService;
import com.auctionhouse.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller")
public class ApiSellerController {

    private final SellerService sellerService;
    private final UserService userService;
    private final ApiMapperService mapper;

    public ApiSellerController(SellerService sellerService, UserService userService, ApiMapperService mapper) {
        this.sellerService = sellerService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiSellerDashboardResponse> dashboard(Authentication authentication) {
        User seller = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(new ApiSellerDashboardResponse(
                mapper.toSellerProfile(seller),
                sellerService.getSellerProducts(seller).stream().map(mapper::toProductView).toList()));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {
        User seller = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(mapper.toSellerProfile(seller));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiMessageResponse> addProduct(Authentication authentication, @Valid @RequestBody ProductRequest request) {
        User seller = userService.getByEmail(authentication.getName());
        sellerService.createProduct(seller, request);
        return ResponseEntity.ok(new ApiMessageResponse("Product submitted for admin approval"));
    }
}
