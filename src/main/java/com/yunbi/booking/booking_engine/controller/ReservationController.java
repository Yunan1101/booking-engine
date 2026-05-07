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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2. 좌석 예매 API", description = "콘서트 좌석 예매 및 동시성 제어 핵심 기능")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "좌석 예매 요청 (Pessimistic Lock 적용)", description = "유저 ID와 좌석 ID를 받아 예매를 처리합니다. 다수가 동시 접근 시 비관적 락을 통해 1명만 성공시키고 나머지는 409 에러를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예매 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (존재하지 않는 유저/좌석)"),
            @ApiResponse(responseCode = "409", description = "이미 다른 사람이 선점한 좌석 (동시성 제어 작동)")
    })
    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserveSeat(request.getUserId(), request.getSeatId());
        return ResponseEntity.ok(new ReservationResponse(reservation.getId(), reservation.getStatus().name()));
    }

    @Getter
    @AllArgsConstructor
    public static class ReservationResponse {
        private Long reservationId;
        private String status;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationRequest {
        private Long userId;
        private Long seatId;
    }
}
