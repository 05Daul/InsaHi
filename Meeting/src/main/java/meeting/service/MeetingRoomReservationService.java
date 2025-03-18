package meeting.service;

import lombok.RequiredArgsConstructor;
import meeting.entity.MeetingRoomReservation;
import meeting.enums.ReservationStatus;
import meeting.repository.MeetingRoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MeetingRoomReservationService {
    @Autowired
    private MeetingRoomReservationRepository reservationRepository;

    public MeetingRoomReservation createReservation(MeetingRoomReservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<MeetingRoomReservation> getReservationsByRoom(String roomNumber) {
        return reservationRepository.findByRoomNumber(roomNumber);
    }

    public List<MeetingRoomReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void updateReservationStatus(Long id, ReservationStatus status) {
        var meetingRoomReservation = reservationRepository.findById(id);

        if (meetingRoomReservation.isPresent()) {
            var reservation = meetingRoomReservation.get();
            reservation.setStatus(status);
        }
    }
}
