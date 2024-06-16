package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationTimeService;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.register(request);
        ReservationTimeResponse reservationTimeResponse = ReservationTimeResponse.from(
                reservationTime.getId(), reservationTime.getStartAt());
        URI location = URI.create("/times/" + reservationTimeResponse.id());
        return ResponseEntity.created(location).body(reservationTimeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findReservationTimes();
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimes.stream()
                .map(reservationTime -> ReservationTimeResponse.from(reservationTime.getId(),
                        reservationTime.getStartAt()))
                .toList();
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
