package com.playdata.Cabinet.service;

import com.playdata.Cabinet.dto.param.CreateMeetingRoomParamDto;
import com.playdata.Cabinet.entity.MeetingRoom;
import com.playdata.Cabinet.repository.MeetingRoomRepository;
import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MeetingRoomService {
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public MeetingRoom createMeetingRoom(CreateMeetingRoomParamDto paramDto) {
        validateAuthority(paramDto.getEmployeeId());

        return meetingRoomRepository.save(paramDto.map());
    }

    private void validateAuthority(String employeeId) {
        var employee = employeeRepository.findByEmployeeId(employeeId);

        if (isValidAuthority(employee.getAuthorityList())) {
           return;
        }

        new Exception("허용되지 않은 권한입니다.");
    }

    private boolean isValidAuthority(Set<Authority> authorityList) {
        return Optional.ofNullable(authorityList)
                .orElse(Set.of())
                .stream()
                .anyMatch(authority -> StringUtils.equals(authority.getAuthorityName(), "ADMIN"));
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
