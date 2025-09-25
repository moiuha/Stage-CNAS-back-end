package com.example.back_end.utils;

import com.example.back_end.dto.RoomDTO;
import com.example.back_end.dto.UserDTO;
import com.example.back_end.entity.Room;
import com.example.back_end.entity.User;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();


    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        return userDTO;
    }


    public static UserDTO mapUserEntityToUserDTOPlusRoom(User user) {
        UserDTO userDTO = mapUserEntityToUserDTO(user);


        if (user.getRoom() != null) {
            userDTO.setCheckInDate(user.getCheckInDate());
            userDTO.setCheckOutDate(user.getCheckOutDate());
            userDTO.setRoom(mapRoomEntityToRoomDTO(user.getRoom()));
        }
        return userDTO;
    }


    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDto = new RoomDTO();
        roomDto.setRoomId(room.getRoomId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomDescription(room.getRoomDescription());
        roomDto.setRoomStatus(room.getRoomStatus());
        roomDto.setRoomImage(room.getRoomImage());
        roomDto.setRoomNumOfBed(room.getRoomNumOfBed());
        roomDto.setRoomFloor(room.getRoomFloor());
        roomDto.setRoomNumber(room.getRoomNumber());
        return roomDto;
    }




    public static RoomDTO mapRoomEntityToRoomDTOPlusUsers(Room room) {
        RoomDTO roomDto = mapRoomEntityToRoomDTO(room);

        if (room.getUsers() != null && !room.getUsers().isEmpty()) {
            roomDto.setUsers(
                    room.getUsers()
                            .stream()
                            .map(Utils::mapUserEntityToUserDTO) // convert each User → UserDTO
                            .collect(Collectors.toList())
            );
        }
        return roomDto;
    }

    public static List<RoomDTO> mapRoomEntitiesToRoomDTOsPlusUsers(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) return Collections.emptyList();
        return rooms.stream()
                .map(Utils::mapRoomEntityToRoomDTOPlusUsers) // reuse your single-entity mapper
                .collect(Collectors.toList());
    }


    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }
}
