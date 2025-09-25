package com.example.back_end.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;
    private RoomDTO room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
