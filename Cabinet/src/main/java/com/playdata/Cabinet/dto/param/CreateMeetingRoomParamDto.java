package com.playdata.Cabinet.dto.param;

import com.playdata.Cabinet.entity.MeetingRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRoomParamDto {
    private String roomNumber;
    private int capacity;
    private String photo;
    private String employeeId;

    public MeetingRoom map() {
        return MeetingRoom.builder()
                .capacity(capacity)
                .photo(photo)
                .roomNumber(roomNumber)
                .build();
    }
}
