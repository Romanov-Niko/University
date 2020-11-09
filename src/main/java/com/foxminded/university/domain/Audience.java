package com.foxminded.university.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "audiences")
public class Audience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Min(value = 1, message = "Room number can not be less than 1")
    @Column(name = "room_number")
    private int roomNumber;
    @Min(value = 1, message = "Capacity can not be less than 1")
    private int capacity;

    public Audience() {
    }

    public Audience(int roomNumber, int capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public Audience(int id, int roomNumber, int capacity) {
        this(roomNumber, capacity);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audience audience = (Audience) o;
        return id == audience.id &&
                roomNumber == audience.roomNumber &&
                capacity == audience.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, capacity);
    }

    @Override
    public String toString() {
        return "Audience{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", capacity=" + capacity +
                '}';
    }
}
