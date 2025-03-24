package com.playdata.Cabinet.controller;

import com.playdata.Cabinet.entity.MeetingRoom;
import com.playdata.Cabinet.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class MeetingRoomController {
    @Autowired
    private MeetingRoomService meetingRoomService;

    @PostMapping
    public ResponseEntity<MeetingRoom> createMeetingRoom(@RequestBody MeetingRoom room) {
        return new ResponseEntity<>(meetingRoomService.createMeetingRoom(room), HttpStatus.CREATED);
    }

    @GetMapping
    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomService.getAllMeetingRooms();
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<MeetingRoom> getMeetingRoom(@PathVariable String roomNumber) {
        return meetingRoomService.getMeetingRoom(roomNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable String roomNumber) {
        meetingRoomService.deleteMeetingRoom(roomNumber);
        return ResponseEntity.noContent().build();
    }
}
