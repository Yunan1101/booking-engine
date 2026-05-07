package com.yunbi.booking.booking_engine.service;

import com.yunbi.booking.booking_engine.domain.Reservation;
import com.yunbi.booking.booking_engine.domain.ReservationStatus;
import com.yunbi.booking.booking_engine.domain.Seat;
import com.yunbi.booking.booking_engine.domain.User;
import com.yunbi.booking.booking_engine.exception.SeatAlreadyReservedException;
import com.yunbi.booking.booking_engine.repository.ReservationRepository;
import com.yunbi.booking.booking_engine.repository.SeatRepository;
import com.yunbi.booking.booking_engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation reserveSeat(Long userId, Long seatId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 2. 좌석 조회 (비관적 락 획득: 이 시점부터 트랜잭션이 끝날 때까지 다른 쓰레드는 이 좌석에 접근 불가)
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));

        // 3. 좌석 상태 검증 및 예약 처리 (이미 예약된 좌석이면 Exception 발생)
        try {
            seat.reserve();
        } catch (IllegalStateException e) {
            throw new SeatAlreadyReservedException("이미 누군가 선점한 좌석입니다.");
        }

        // 4. 예약 정보 생성 및 저장
        Reservation reservation = Reservation.builder()
                .user(user)
                .seat(seat)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.SUCCESS)
                .build();

        return reservationRepository.save(reservation);
    }
}
