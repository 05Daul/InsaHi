package com.playdata.Cabinet.repository;

import com.playdata.Cabinet.entity.MeetingRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomReservationRepository extends JpaRepository<MeetingRoomReservation, Long> {
    List<MeetingRoomReservation> findByRoomNumber(String roomNumber);
}
