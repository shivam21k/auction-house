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

    public List<Auction> getAwaitingAdminApprovalAuctions() {
        refreshAuctionStates();
        return auctionRepository.findByStatusOrderByEndTimeDesc(AuctionStatus.AWAITING_ADMIN_APPROVAL);
    }

    @Transactional
    public void finalizeAuctionOwnership(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        if (auction.getStatus() != AuctionStatus.AWAITING_ADMIN_APPROVAL) {
            throw new IllegalArgumentException("Auction is not pending admin approval");
        }

        auction.setStatus(AuctionStatus.ENDED);
        auction.getProduct().setStatus(ProductStatus.CLOSED);
        auctionRepository.save(auction);
    }

    @Transactional
    public String startTestLiveAuctionNow() {
        LocalDateTime now = LocalDateTime.now();

        Optional<Auction> liveAuction = auctionRepository.findFirstByStatusOrderByStartTimeAsc(AuctionStatus.LIVE);
        if (liveAuction.isPresent()) {
            Auction current = liveAuction.get();
            current.setEndTime(now.plusMinutes(1));
            auctionRepository.save(current);
            return "Existing live auction timer reset to 1 minute";
        }

        List<Auction> upcoming = auctionRepository.findByStatusOrderByStartTimeAsc(AuctionStatus.UPCOMING);
        if (!upcoming.isEmpty()) {
            Auction toStart = upcoming.get(0);
            toStart.setStartTime(now);
            toStart.setEndTime(now.plusMinutes(1));
            toStart.setStatus(AuctionStatus.LIVE);
            toStart.getProduct().setStatus(ProductStatus.LIVE);
            auctionRepository.save(toStart);
            return "Upcoming auction moved to live for 1 minute";
        }

        List<Product> pendingProducts = productRepository.findByStatusOrderByCreatedAtDesc(ProductStatus.PENDING_APPROVAL);
        if (!pendingProducts.isEmpty()) {
            Product product = pendingProducts.get(0);
            product.setStatus(ProductStatus.LIVE);
            productRepository.save(product);

            Auction auction = new Auction();
            auction.setProduct(product);
            auction.setStartTime(now);
            auction.setEndTime(now.plusMinutes(1));
            auction.setCurrentBid(product.getBasePrice());
            auction.setStatus(AuctionStatus.LIVE);
            auctionRepository.save(auction);
            return "Pending product promoted to a live 1-minute test auction";
        }

        throw new IllegalArgumentException("No product available to start a live test auction");
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void refreshAuctionStates() {
        LocalDateTime now = LocalDateTime.now();

        auctionRepository.findFirstByStatusOrderByStartTimeAsc(AuctionStatus.LIVE).ifPresent(live -> {
            LocalDateTime forcedEnd = now.plusMinutes(1);
            if (live.getEndTime().isAfter(forcedEnd)) {
                live.setEndTime(forcedEnd);
                auctionRepository.save(live);
            }
        });

        List<Auction> endedLiveAuctions = auctionRepository.findByStatusAndEndTimeBefore(AuctionStatus.LIVE, now);
        for (Auction auction : endedLiveAuctions) {
            auction.setStatus(AuctionStatus.AWAITING_ADMIN_APPROVAL);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.AWAITING_ADMIN_APPROVAL);
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
                next.setEndTime(now.plusMinutes(1));
                next.getProduct().setStatus(ProductStatus.LIVE);
            }
        }

        auctionRepository.saveAll(endedLiveAuctions);
        productRepository.flush();
    }
}
