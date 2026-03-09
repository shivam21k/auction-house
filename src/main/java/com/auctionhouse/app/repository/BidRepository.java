package com.auctionhouse.app.repository;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.Bid;
import com.auctionhouse.app.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findTopByAuctionOrderByAmountDescBidTimeAsc(Auction auction);
    List<Bid> findByBuyerOrderByBidTimeDesc(User buyer);
}
