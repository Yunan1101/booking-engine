package com.yunbi.booking.booking_engine.repository;

import com.yunbi.booking.booking_engine.domain.ConcertSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertSessionRepository extends JpaRepository<ConcertSession, Long> {
    List<ConcertSession> findByConcertId(Long concertId);
}
