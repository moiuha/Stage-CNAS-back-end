package com.example.back_end.dto;


import lombok.Data;

@Data
public class CancelBookingRequest {
    private Long userId;
    private Long roomId;
}
