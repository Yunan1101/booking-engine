package com.yunbi.booking.booking_engine.config;

import com.yunbi.booking.booking_engine.domain.Concert;
import com.yunbi.booking.booking_engine.domain.ConcertSession;
import com.yunbi.booking.booking_engine.domain.Seat;
import com.yunbi.booking.booking_engine.domain.SeatStatus;
import com.yunbi.booking.booking_engine.domain.User;
import com.yunbi.booking.booking_engine.repository.ConcertRepository;
import com.yunbi.booking.booking_engine.repository.ConcertSessionRepository;
import com.yunbi.booking.booking_engine.repository.SeatRepository;
import com.yunbi.booking.booking_engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final ConcertSessionRepository concertSessionRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 데이터가 이미 존재하면 중복 생성하지 않음
        if (userRepository.count() > 0) {
            return;
        }

        // 1. 유저 1명 생성 (아이디: 1L)
        User user = User.builder()
                .name("yunbi")
                .email("yunbi@test.com")
                .password("password123")
                .build();
        userRepository.save(user);

        // 2. 콘서트 1개 생성 (아이디: 1L)
        Concert concert = Concert.builder()
                .title("아이유 2026 월드투어")
                .description("역대급 스케일의 크리스마스 콘서트")
                .build();
        concertRepository.save(concert);

        // 3. 콘서트 회차 1개 생성 (아이디: 1L)
        ConcertSession session = ConcertSession.builder()
                .concert(concert)
                .sessionDate(LocalDateTime.of(2026, 12, 24, 19, 0))
                .build();
        concertSessionRepository.save(session);

        // 4. 좌석 50개 생성 (VIP석 1번부터 50번까지, 모두 AVAILABLE)
        for (int i = 1; i <= 50; i++) {
            Seat seat = Seat.builder()
                    .concertSession(session)
                    .seatNumber("VIP-" + i)
                    .price(150000)
                    .status(SeatStatus.AVAILABLE)
                    .build();
            seatRepository.save(seat);
        }

        System.out.println("✅ 더미 데이터 세팅 완료: 유저 1명, 콘서트 1개, 회차 1개, 좌석 50개 생성됨!");
    }
}
