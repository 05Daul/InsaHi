package meeting.service;

import lombok.RequiredArgsConstructor;
import meeting.entity.MeetingRoom;
import meeting.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeetingRoomService {
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    public MeetingRoom createMeetingRoom(MeetingRoom room) {
        return meetingRoomRepository.save(room);
    }

    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomRepository.findAll();
    }

    public Optional<MeetingRoom> getMeetingRoom(String roomNumber) {
        return meetingRoomRepository.findById(roomNumber);
    }

    public void deleteMeetingRoom(String roomNumber) {
        meetingRoomRepository.deleteById(roomNumber);
    }
}
