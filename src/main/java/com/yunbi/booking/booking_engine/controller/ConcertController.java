package com.yunbi.booking.booking_engine.controller;

import com.yunbi.booking.booking_engine.domain.Concert;
import com.yunbi.booking.booking_engine.domain.ConcertSession;
import com.yunbi.booking.booking_engine.domain.Seat;
import com.yunbi.booking.booking_engine.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/concerts")
    public ResponseEntity<List<Concert>> getConcerts() {
        return ResponseEntity.ok(concertService.getAllConcerts());
    }

    @GetMapping("/concerts/{id}/sessions")
    public ResponseEntity<List<ConcertSession>> getSessions(@PathVariable("id") Long concertId) {
        return ResponseEntity.ok(concertService.getSessions(concertId));
    }

    @GetMapping("/sessions/{id}/seats")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable("id") Long sessionId) {
        return ResponseEntity.ok(concertService.getAvailableSeats(sessionId));
    }
}
