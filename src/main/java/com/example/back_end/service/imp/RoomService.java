package com.example.back_end.service.imp;

import com.example.back_end.dto.Response;
import com.example.back_end.dto.RoomDTO;
import com.example.back_end.entity.Room;
import com.example.back_end.entity.User;
import com.example.back_end.exception.OurException;
import com.example.back_end.repo.RoomRepository;
import com.example.back_end.repo.UserRepository;
import com.example.back_end.service.interfac.IRoomService;
import com.example.back_end.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;






    @Override
    public Response addNewRoom(String photo, String roomType, BigDecimal  roomFloor, String roomDescription, BigDecimal  roomNumOfBed,BigDecimal roomNumber) {
        Response response = new Response();

        try {
//            String imageUrl = awsS3Service.saveImageToS3(photo);
            Room room = new Room();
            room.setRoomImage(photo);
            room.setRoomType(roomType);
            room.setRoomFloor(roomFloor);
            room.setRoomDescription(roomDescription);
            room.setRoomNumOfBed(roomNumOfBed);
            room.setRoomNumber(roomNumber);
            room.setRoomStatus("Disponible");
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;

    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"));
            List<RoomDTO> roomDTOList = Utils.mapRoomEntitiesToRoomDTOsPlusUsers(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }



    @Override
    @Transactional
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not found"));

            // If there are users linked to the room, remove the association first
            if (room.getUsers() != null && !room.getUsers().isEmpty()) {
                List<User> users = room.getUsers();
                // clear room reference for each user
                users.forEach(u -> u.setRoom(null));
                // persist changes in batch
                userRepository.saveAll(users);
                // optionally clear the room's users list
                room.getUsers().clear();
                roomRepository.save(room);
            }

            // now safe to delete the room
            roomRepository.deleteById(roomId);

            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String roomDescription, String roomType, BigDecimal roomFloor, BigDecimal roomNumOfBed, String roomImage, BigDecimal roomNumber) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));


            int currentUsersCount = (room.getUsers() == null) ? 0 : room.getUsers().size();


            if (roomNumOfBed != null) {
                BigDecimal usersCountBD = BigDecimal.valueOf(currentUsersCount);


                if (roomNumOfBed.compareTo(usersCountBD) < 0) {

                    throw new OurException("Vous ne pouvez pas diminuer le nombre de lits en dessous du nombre d'occupants.");
                }


                if (roomNumOfBed.compareTo(usersCountBD) == 0) {
                    room.setRoomStatus("Occupée");
                } else {
                    room.setRoomStatus("Disponible");
                }

                room.setRoomNumOfBed(roomNumOfBed);
            }

            if (roomType != null) room.setRoomType(roomType);
            if (roomFloor != null) room.setRoomFloor(roomFloor);
            if (roomDescription != null) room.setRoomDescription(roomDescription);
            if (roomImage != null) room.setRoomImage(roomImage);
            if (roomNumber != null) room.setRoomNumber(roomNumber);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusUsers(room);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

//    @Override
//    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
//        Response response = new Response();
//
//        try {
//            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
//            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
//            response.setStatusCode(200);
//            response.setMessage("successful");
//            response.setRoomList(roomDTOList);
//
//        } catch (Exception e) {
//            response.setStatusCode(500);
//            response.setMessage("Error saving getting available rooms by type and date " + e.getMessage());
//        }
//        return response;
//    }

    @Override
    public Response getAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response bookRoom(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            Room newRoom = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not found"));

            if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
                throw new OurException("Invalid check-in/check-out dates");
            }


            if (newRoom.getUsers() == null) {
                newRoom.setUsers(new java.util.ArrayList<>());
            }


            BigDecimal newRoomBeds = newRoom.getRoomNumOfBed() == null ? BigDecimal.ZERO : newRoom.getRoomNumOfBed();
            int newRoomUsersCount = newRoom.getUsers().size();
            if (BigDecimal.valueOf(newRoomUsersCount).compareTo(newRoomBeds) >= 0) {
                throw new OurException("Room is not available");
            }

            Room previousRoom = user.getRoom();


            if (previousRoom != null && previousRoom.getRoomId() != null && previousRoom.getRoomId().equals(roomId)) {
                user.setCheckInDate(checkIn);
                user.setCheckOutDate(checkOut);
                userRepository.save(user);


                recomputeRoomStatus(newRoom);
                roomRepository.save(newRoom);

                response.setStatusCode(200);
                response.setMessage("Room booked successfully");
                response.setUser(Utils.mapUserEntityToUserDTOPlusRoom(user));
                return response;
            }


            if (previousRoom != null && previousRoom.getRoomId() != null) {

                Room prev = roomRepository.findById(previousRoom.getRoomId())
                        .orElse(null);
                if (prev != null && prev.getUsers() != null) {
                    prev.getUsers().removeIf(u -> u.getUserId() != null && u.getUserId().equals(userId));

                    recomputeRoomStatus(prev);
                    roomRepository.save(prev);
                }
            }


            user.setRoom(newRoom);
            user.setCheckInDate(checkIn);
            user.setCheckOutDate(checkOut);
            userRepository.save(user);


            if (!newRoom.getUsers().stream().anyMatch(u -> u.getUserId() != null && u.getUserId().equals(userId))) {
                newRoom.getUsers().add(user);
            }


            recomputeRoomStatus(newRoom);
            roomRepository.save(newRoom);

            response.setStatusCode(200);
            response.setMessage("Room booked successfully");
            response.setUser(Utils.mapUserEntityToUserDTOPlusRoom(user));

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error booking room: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response cancelBooking(Long userId, Long roomId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not found"));


            if (user.getRoom() == null || user.getRoom().getRoomId() == null
                    || !user.getRoom().getRoomId().equals(roomId)) {
                throw new OurException("This user does not have this room booked");
            }


            user.setRoom(null);
            user.setCheckInDate(null);
            user.setCheckOutDate(null);
            userRepository.save(user);


            if (room.getUsers() != null) {
                room.getUsers().removeIf(u -> u.getUserId() != null && u.getUserId().equals(userId));
            }



            roomRepository.save(room);

            response.setStatusCode(200);
            response.setMessage("Booking cancelled successfully");

            response.setUser(Utils.mapUserEntityToUserDTO(user));

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error cancelling booking: " + e.getMessage());
        }

        return response;
    }





    private void recomputeRoomStatus(Room room) {
        if (room == null) return;

        String currentStatus = room.getRoomStatus();

        int usersCount = (room.getUsers() == null) ? 0 : room.getUsers().size();
        BigDecimal beds = room.getRoomNumOfBed() == null ? BigDecimal.ZERO : room.getRoomNumOfBed();
        BigDecimal usersBD = BigDecimal.valueOf(usersCount);

        if (beds.compareTo(usersBD) <= 0) {

            room.setRoomStatus("Occupée");
        } else {
            room.setRoomStatus("Disponible");
        }
    }


}
