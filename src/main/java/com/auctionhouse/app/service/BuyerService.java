package com.auctionhouse.app.service;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.AuctionStatus;
import com.auctionhouse.app.model.Bid;
import com.auctionhouse.app.model.User;
import com.auctionhouse.app.repository.AuctionRepository;
import com.auctionhouse.app.repository.BidRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class BuyerService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    public BuyerService(AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @Transactional
    public void placeBid(Long auctionId, BigDecimal amount, User buyer) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        if (auction.getStatus() != AuctionStatus.LIVE) {
            throw new IllegalArgumentException("Auction is not live");
        }

        if (amount.compareTo(auction.getCurrentBid()) <= 0) {
            throw new IllegalArgumentException("Bid amount must be greater than current bid");
        }

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBuyer(buyer);
        bid.setAmount(amount);

        auction.setCurrentBid(amount);
        auction.setWinner(buyer);

        bidRepository.save(bid);
        auctionRepository.save(auction);
    }
}
