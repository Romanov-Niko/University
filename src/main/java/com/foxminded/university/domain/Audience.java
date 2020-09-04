package com.foxminded.university.domain;

public class Audience {

    private Integer roomNumber;
    private int capacity;

    public Audience(Integer roomNumber, Integer capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
