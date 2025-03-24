package com.playdata.Chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomCreateRequest {
    private String roomName;
    private List<String> members;
    private String creatorName;
}
