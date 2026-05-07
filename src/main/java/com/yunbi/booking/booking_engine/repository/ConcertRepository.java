package com.yunbi.booking.booking_engine.repository;

import com.yunbi.booking.booking_engine.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
