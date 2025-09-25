package com.example.back_end.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class RoomDTO {
    private Long roomId;
    private String roomType;
    private String roomDescription;
    private String roomStatus;
    private String roomImage;
    private BigDecimal roomNumOfBed;
    private BigDecimal  roomFloor;
    private BigDecimal roomNumber;
    private List<UserDTO> users;
}
