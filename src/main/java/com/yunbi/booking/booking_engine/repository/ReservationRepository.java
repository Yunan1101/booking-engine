package com.yunbi.booking.booking_engine.repository;

import com.yunbi.booking.booking_engine.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
