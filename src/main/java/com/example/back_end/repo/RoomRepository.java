package com.example.back_end.repo;

import com.example.back_end.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

//    @Query("""
//    SELECT r FROM Room r
//    WHERE r.roomType LIKE %:roomType%
//      AND r.roomStatus = 'available'
//      AND (r.checkInDate IS NULL OR r.checkInDate > :checkOutDate)
//      AND (r.checkOutDate IS NULL OR r.checkOutDate < :checkInDate)
//    """)
//    List<Room> findAvailableRoomsByDatesAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    @Query("SELECT r FROM Room r WHERE r.roomStatus = 'AVAILABLE'")
    List<Room> getAllAvailableRooms();
}
