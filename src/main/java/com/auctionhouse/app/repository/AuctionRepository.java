package com.auctionhouse.app.repository;

import com.auctionhouse.app.model.Auction;
import com.auctionhouse.app.model.AuctionStatus;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findFirstByStatusOrderByStartTimeAsc(AuctionStatus status);
    List<Auction> findByStatusOrderByStartTimeAsc(AuctionStatus status);
    List<Auction> findByStatusOrderByEndTimeDesc(AuctionStatus status);
    List<Auction> findByStatusAndStartTimeAfterOrderByStartTimeAsc(AuctionStatus status, LocalDateTime time);
    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, LocalDateTime time);
    List<Auction> findByStatusAndStartTimeLessThanEqualOrderByStartTimeAsc(AuctionStatus status, LocalDateTime time);
    long countByStatus(AuctionStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Auction a where a.id = :id")
    Optional<Auction> findByIdForUpdate(@Param("id") Long id);
}
