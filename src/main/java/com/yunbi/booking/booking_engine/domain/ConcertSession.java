package com.yunbi.booking.booking_engine.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(nullable = false)
    private LocalDateTime sessionDate;

    @Builder
    public ConcertSession(Concert concert, LocalDateTime sessionDate) {
        this.concert = concert;
        this.sessionDate = sessionDate;
    }
}
