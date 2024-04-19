package roomescape.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;

public record ReservationRequest(String name, LocalDate date, LocalTime time) {
    public Reservation toReservation() {
        return new Reservation(name, LocalDateTime.of(date, time));
    }
}
