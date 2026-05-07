package com.yunbi.booking.booking_engine.dto;

import com.yunbi.booking.booking_engine.domain.Seat;
import com.yunbi.booking.booking_engine.domain.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private Long concertSessionId;
    private String seatNumber;
    private Integer price;
    private SeatStatus status;

    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getConcertSession().getId(),
                seat.getSeatNumber(),
                seat.getPrice(),
                seat.getStatus()
        );
    }
}
