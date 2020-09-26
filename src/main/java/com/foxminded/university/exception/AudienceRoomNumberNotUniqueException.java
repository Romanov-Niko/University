package com.foxminded.university.exception;

public class AudienceRoomNumberNotUniqueException extends RuntimeException {

    public AudienceRoomNumberNotUniqueException(String errorMessage) {
        super(errorMessage);
    }
}
