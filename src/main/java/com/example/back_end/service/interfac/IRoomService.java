package com.example.back_end.service.interfac;

import com.example.back_end.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {



    Response addNewRoom(String photo, String roomType, BigDecimal  roomFloor, String roomDescription, BigDecimal  roomNumOfBed,BigDecimal roomNumber);

    Response getAllRooms();
    List<String> getAllRoomTypes();
    Response deleteRoom(Long roomId );
    Response updateRoom(Long roomId,String roomDescription, String roomType,BigDecimal  roomFloor,BigDecimal  roomNumOfBed, String roomImage , BigDecimal roomNumber);
    Response getRoomById(Long roomId );
//    Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    Response getAvailableRooms();
    Response bookRoom(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut);
    Response cancelBooking(Long userID, Long roomId );


}
