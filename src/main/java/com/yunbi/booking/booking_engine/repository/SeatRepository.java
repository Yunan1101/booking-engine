package com.yunbi.booking.booking_engine.repository;

import com.yunbi.booking.booking_engine.domain.Seat;
import com.yunbi.booking.booking_engine.domain.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // 인덱스를 활용하여 특정 회차의 상태별 좌석을 빠르게 조회
    List<Seat> findByConcertSessionIdAndStatus(Long concertSessionId, SeatStatus status);

    // [핵심] 비관적 락(X-Lock)을 걸어 예매 시 동시성 이슈를 완벽하게 차단
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findByIdForUpdate(@Param("id") Long id);
}
