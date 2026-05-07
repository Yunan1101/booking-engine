package com.yunbi.booking.booking_engine.controller;

import com.yunbi.booking.booking_engine.domain.Reservation;
import com.yunbi.booking.booking_engine.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserveSeat(request.getUserId(), request.getSeatId());
        return ResponseEntity.ok(reservation);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationRequest {
        private Long userId;
        private Long seatId;
    }
}
