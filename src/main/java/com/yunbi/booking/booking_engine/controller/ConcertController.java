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

import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.yunbi.booking.booking_engine.dto.SeatResponse;

@Tag(name = "1. 콘서트 조회 API", description = "콘서트 목록, 회차, 좌석 등을 조회하는 기능")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @Operation(summary = "전체 콘서트 목록 조회", description = "등록된 모든 콘서트 정보를 조회합니다.")
    @GetMapping("/concerts")
    public ResponseEntity<List<Concert>> getConcerts() {
        return ResponseEntity.ok(concertService.getAllConcerts());
    }

    @Operation(summary = "콘서트 회차(세션) 조회", description = "특정 콘서트의 회차 일정 목록을 조회합니다.")
    @GetMapping("/concerts/{id}/sessions")
    public ResponseEntity<List<ConcertSession>> getSessions(@PathVariable("id") Long concertId) {
        return ResponseEntity.ok(concertService.getSessions(concertId));
    }

    @Operation(summary = "예약 가능한 좌석 조회", description = "특정 콘서트 회차에서 현재 예매 가능한(AVAILABLE) 빈 좌석 목록을 조회합니다.")
    @GetMapping("/sessions/{id}/seats")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable("id") Long sessionId) {
        List<SeatResponse> seatResponses = concertService.getAvailableSeats(sessionId)
                .stream()
                .map(SeatResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(seatResponses);
    }
}
