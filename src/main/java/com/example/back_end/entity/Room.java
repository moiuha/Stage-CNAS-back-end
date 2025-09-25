package com.example.back_end.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="rooms")

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String roomType;
    private String roomDescription;
    private String roomStatus;
    private String roomImage;




    private BigDecimal roomNumber;
    private BigDecimal roomNumOfBed;
    private BigDecimal  roomFloor;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomType='" + roomType + '\'' +
                ", roomDescription='" + roomDescription + '\'' +
                ", roomStatus='" + roomStatus + '\'' +
                ", roomImage='" + roomImage + '\'' +
                ", numOfBed=" + roomNumOfBed +

                '}';
    }
}
