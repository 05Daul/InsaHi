package meeting.controller;

import lombok.RequiredArgsConstructor;
import meeting.entity.MeetingRoomReservation;
import meeting.enums.ReservationStatus;
import meeting.service.MeetingRoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class MeetingRoomReservationController {
    @Autowired
    private MeetingRoomReservationService reservationService;

    @PostMapping
    public ResponseEntity<MeetingRoomReservation> createReservation(@RequestBody MeetingRoomReservation reservation) {
        return new ResponseEntity<>(reservationService.createReservation(reservation), HttpStatus.CREATED);
    }

    @GetMapping
    public List<MeetingRoomReservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateReservationStatus(@PathVariable Long id, @RequestBody ReservationStatus status) {
        reservationService.updateReservationStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
