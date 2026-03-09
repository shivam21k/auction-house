package com.auctionhouse.app.service;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.AuctionStatus;
import com.auctionhouse.app.model.Product;
import com.auctionhouse.app.model.ProductStatus;
import com.auctionhouse.app.repository.AuctionRepository;
import com.auctionhouse.app.repository.BidRepository;
import com.auctionhouse.app.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;

    public AuctionService(
            AuctionRepository auctionRepository,
            BidRepository bidRepository,
            ProductRepository productRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
    }

    public Optional<Auction> getCurrentAuction() {
        refreshAuctionStates();
        return auctionRepository.findFirstByStatusOrderByStartTimeAsc(AuctionStatus.LIVE);
    }

    public List<Auction> getUpcomingAuctions() {
        refreshAuctionStates();
        return auctionRepository.findByStatusAndStartTimeAfterOrderByStartTimeAsc(AuctionStatus.UPCOMING, LocalDateTime.now());
    }

    public List<Auction> getAllUpcomingAuctions() {
        refreshAuctionStates();
        return auctionRepository.findByStatusOrderByStartTimeAsc(AuctionStatus.UPCOMING);
    }

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void refreshAuctionStates() {
        LocalDateTime now = LocalDateTime.now();

        List<Auction> endedLiveAuctions = auctionRepository.findByStatusAndEndTimeBefore(AuctionStatus.LIVE, now);
        for (Auction auction : endedLiveAuctions) {
            auction.setStatus(AuctionStatus.ENDED);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.CLOSED);
            bidRepository.findTopByAuctionOrderByAmountDescBidTimeAsc(auction)
                    .ifPresent(bid -> auction.setWinner(bid.getBuyer()));
        }

        boolean hasLiveAuction = auctionRepository.findFirstByStatusOrderByStartTimeAsc(AuctionStatus.LIVE).isPresent();
        if (!hasLiveAuction) {
            List<Auction> eligibleUpcoming = auctionRepository
                    .findByStatusAndStartTimeLessThanEqualOrderByStartTimeAsc(AuctionStatus.UPCOMING, now);
            if (!eligibleUpcoming.isEmpty()) {
                Auction next = eligibleUpcoming.get(0);
                next.setStatus(AuctionStatus.LIVE);
                next.getProduct().setStatus(ProductStatus.LIVE);
            }
        }

        auctionRepository.saveAll(endedLiveAuctions);
        productRepository.flush();
    }
}
