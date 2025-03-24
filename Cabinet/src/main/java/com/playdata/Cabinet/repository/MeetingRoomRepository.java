package com.playdata.Cabinet.repository;

import com.playdata.Cabinet.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, String> {
}
