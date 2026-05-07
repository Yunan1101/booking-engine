package com.yunbi.booking.booking_engine.service;

import com.yunbi.booking.booking_engine.domain.Concert;
import com.yunbi.booking.booking_engine.domain.ConcertSession;
import com.yunbi.booking.booking_engine.domain.Seat;
import com.yunbi.booking.booking_engine.domain.SeatStatus;
import com.yunbi.booking.booking_engine.repository.ConcertRepository;
import com.yunbi.booking.booking_engine.repository.ConcertSessionRepository;
import com.yunbi.booking.booking_engine.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertSessionRepository concertSessionRepository;
    private final SeatRepository seatRepository;

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    public List<ConcertSession> getSessions(Long concertId) {
        return concertSessionRepository.findByConcertId(concertId);
    }

    public List<Seat> getAvailableSeats(Long sessionId) {
        // 복합 인덱스를 활용한 빠른 빈 좌석 조회
        return seatRepository.findByConcertSessionIdAndStatus(sessionId, SeatStatus.AVAILABLE);
    }
}
