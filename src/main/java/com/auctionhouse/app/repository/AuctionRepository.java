package com.auctionhouse.app.repository;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.AuctionStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findFirstByStatusOrderByStartTimeAsc(AuctionStatus status);
    List<Auction> findByStatusOrderByStartTimeAsc(AuctionStatus status);
    List<Auction> findByStatusAndStartTimeAfterOrderByStartTimeAsc(AuctionStatus status, LocalDateTime time);
    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, LocalDateTime time);
    List<Auction> findByStatusAndStartTimeLessThanEqualOrderByStartTimeAsc(AuctionStatus status, LocalDateTime time);
    long countByStatus(AuctionStatus status);
}
