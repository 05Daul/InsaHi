package com.playdata.Cabinet.service;

import com.playdata.Cabinet.entity.MeetingRoomReservation;
import com.playdata.Cabinet.enums.ReservationStatus;
import com.playdata.Cabinet.repository.MeetingRoomReservationRepository;
import lombok.RequiredArgsConstructor;

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
    //동시간때에 예약 관련
    @Transactional
    public void updateReservationStatus(Long id, ReservationStatus status) {
        Optional<MeetingRoomReservation> meetingRoomReservation = reservationRepository.findById(id);

        if (meetingRoomReservation.isPresent()) {
            MeetingRoomReservation reservation = meetingRoomReservation.get();
            reservation.setStatus(status);
        }
    }
}
