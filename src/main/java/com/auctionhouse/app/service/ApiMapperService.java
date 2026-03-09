package com.auctionhouse.app.service;

import com.auctionhouse.app.dto.api.ApiAuctionView;
import com.auctionhouse.app.dto.api.ApiProductApprovalView;
import com.auctionhouse.app.dto.api.ApiProductView;
import com.auctionhouse.app.dto.api.ApiSellerProfileResponse;
import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.Product;
import com.auctionhouse.app.model.User;
import org.springframework.stereotype.Service;

@Service
public class ApiMapperService {

    public ApiAuctionView toAuctionView(Auction auction) {
        return new ApiAuctionView(
                auction.getId(),
                auction.getProduct().getTitle(),
                auction.getProduct().getDescription(),
                auction.getProduct().getCategory(),
                auction.getProduct().getBasePrice(),
                auction.getCurrentBid(),
                auction.getStartTime(),
                auction.getEndTime(),
                auction.getProduct().getSeller().getFullName(),
                auction.getStatus().name()
        );
    }

    public ApiProductView toProductView(Product product) {
        return new ApiProductView(
                product.getId(),
                product.getTitle(),
                product.getCategory(),
                product.getBasePrice(),
                product.getReservePrice(),
                product.getStatus().name(),
                product.getRequestedSlotStart(),
                product.getRequestedSlotEnd()
        );
    }

    public ApiProductApprovalView toApprovalView(Product product) {
        return new ApiProductApprovalView(
                product.getId(),
                product.getTitle(),
                product.getBasePrice(),
                product.getRequestedSlotStart(),
                product.getRequestedSlotEnd(),
                product.getSeller().getFullName(),
                product.getSeller().getEmail()
        );
    }

    public ApiSellerProfileResponse toSellerProfile(User user) {
        return new ApiSellerProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getCompanyName()
        );
    }
}
