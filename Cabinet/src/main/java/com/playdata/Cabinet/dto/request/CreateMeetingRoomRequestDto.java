package com.playdata.Cabinet.dto.request;

import com.playdata.Cabinet.dto.param.CreateMeetingRoomParamDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMeetingRoomRequestDto {
    private String roomNumber;
    private int capacity;
    private String photo;
    private String employeeId;

    public CreateMeetingRoomParamDto map() {
        return CreateMeetingRoomParamDto.builder()
                .capacity(capacity)
                .photo(photo)
                .roomNumber(roomNumber)
                .employeeId(employeeId)
                .build();
    }
}
