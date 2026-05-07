package com.yunbi.booking.booking_engine.service;

import com.yunbi.booking.booking_engine.domain.*;
import com.yunbi.booking.booking_engine.exception.SeatAlreadyReservedException;
import com.yunbi.booking.booking_engine.repository.ConcertRepository;
import com.yunbi.booking.booking_engine.repository.ConcertSessionRepository;
import com.yunbi.booking.booking_engine.repository.SeatRepository;
import com.yunbi.booking.booking_engine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertSessionRepository concertSessionRepository;

    @Autowired
    private SeatRepository seatRepository;

    private Long testUserId;
    private Long testSeatId;

    @BeforeEach
    void setUp() {
        // 테스트 격리를 위해 더미 데이터 1세트 생성
        User user = User.builder()
                .name("testUser")
                .email("test@test.com")
                .password("password123")
                .build();
        userRepository.save(user);
        testUserId = user.getId();

        Concert concert = Concert.builder()
                .title("Test Concert")
                .description("Test Description")
                .build();
        concertRepository.save(concert);

        ConcertSession session = ConcertSession.builder()
                .concert(concert)
                .sessionDate(LocalDateTime.now().plusDays(10))
                .build();
        concertSessionRepository.save(session);

        Seat seat = Seat.builder()
                .concertSession(session)
                .seatNumber("TEST-VIP-1")
                .price(100000)
                .status(SeatStatus.AVAILABLE)
                .build();
        seatRepository.save(seat);
        testSeatId = seat.getId();
    }

    @Test
    @DisplayName("100명의 유저가 동시에 예매를 요청하면 단 1명만 성공해야 한다 (비관적 락 검증)")
    void concurrentReservationTest() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(1); // 100명이 동시에 출발선에 서기 위함
        CountDownLatch doneLatch = new CountDownLatch(threadCount); // 100명이 모두 끝날 때까지 기다리기 위함

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    latch.await(); // 여기서 대기하다가 latch.countDown()이 호출되면 동시에 실행됨
                    reservationService.reserveSeat(testUserId, testSeatId);
                    successCount.incrementAndGet();
                } catch (SeatAlreadyReservedException e) {
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("기타 에러 발생: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        latch.countDown(); // 100개의 스레드 동시 출발! (총성)
        doneLatch.await(); // 100개의 요청이 모두 처리될 때까지 메인 스레드 대기

        // then
        assertEquals(1, successCount.get(), "성공 횟수는 정확히 1이어야 합니다.");
        assertEquals(99, failCount.get(), "실패(SeatAlreadyReservedException) 횟수는 정확히 99이어야 합니다.");
        
        // 추가 검증: DB에 저장된 좌석 상태가 RESERVED 인지 확인
        Seat resultSeat = seatRepository.findById(testSeatId).orElseThrow();
        assertEquals(SeatStatus.RESERVED, resultSeat.getStatus());
    }
}
