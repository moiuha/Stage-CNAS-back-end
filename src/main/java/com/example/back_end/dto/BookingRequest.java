package com.example.back_end.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class BookingRequest {



    private Long userId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;



}
