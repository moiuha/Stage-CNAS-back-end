package com.example.back_end.controller;


import com.example.back_end.dto.BookingRequest;
import com.example.back_end.dto.CancelBookingRequest;
import com.example.back_end.dto.Response;
import com.example.back_end.service.interfac.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {


    @Autowired
    private IRoomService roomService;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false) String photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomFloor", required = false) BigDecimal  roomFloor,
            @RequestParam(value = "roomDescription", required = false, defaultValue = "0") String roomDescription,
            @RequestParam(value = "roomNumOfBed", required = false, defaultValue = "0") BigDecimal  roomNumOfBed,
            @RequestParam(value = "roomNumber", required = false, defaultValue = "0") BigDecimal  roomNumber
    ) {

        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomFloor == null ) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields(photo, roomType,roomFloor )");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.addNewRoom(photo, roomType, roomFloor, roomDescription, roomNumOfBed,roomNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }


    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms() {
        Response response = roomService.getAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

//    @GetMapping("/available-rooms-by-date-and-type")
//    public ResponseEntity<Response> getAvailableRoomsByDateAndType(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
//            @RequestParam(required = false) String roomType
//    ) {
//        if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null) {
//            Response response = new Response();
//            response.setStatusCode(400);
//            response.setMessage("Please provide values for all fields(checkInDate, roomType,checkOutDate)");
//            return ResponseEntity.status(response.getStatusCode()).body(response);
//        }
//        Response response = roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }


    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(@PathVariable Long roomId,
                                               @RequestParam(value = "roomImage", required = false) String roomImage,
                                               @RequestParam(value = "roomType", required = false) String roomType,
                                               @RequestParam(value = "roomFloor", required = false) BigDecimal  roomFloor,
                                               @RequestParam(value = "roomDescription", required = false) String roomDescription,
                                               @RequestParam(value = "roomNumOfBed", required = false) BigDecimal  roomNumOfBed,
                                               @RequestParam(value = "roomNumber", required = false) BigDecimal  roomNumber

    ) {
        Response response = roomService.updateRoom(roomId, roomDescription, roomType, roomFloor,roomNumOfBed, roomImage, roomNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @PostMapping("/book")
    public ResponseEntity<Response> bookRoom(@RequestBody BookingRequest request) {

        Response response = roomService.bookRoom(request.getUserId(), request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Response> cancelBooking(@RequestBody CancelBookingRequest request) {
        Response response = roomService.cancelBooking(request.getUserId(), request.getRoomId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
