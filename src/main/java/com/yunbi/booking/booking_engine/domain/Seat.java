package com.yunbi.booking.booking_engine.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "seat",
    indexes = {
        @Index(name = "idx_session_status", columnList = "concert_session_id, status")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_session_id", nullable = false)
    private ConcertSession concertSession;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Builder
    public Seat(ConcertSession concertSession, String seatNumber, Integer price, SeatStatus status) {
        this.concertSession = concertSession;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public void reserve() {
        if (this.status == SeatStatus.RESERVED) {
            throw new IllegalStateException("Already reserved seat.");
        }
        this.status = SeatStatus.RESERVED;
    }
}
