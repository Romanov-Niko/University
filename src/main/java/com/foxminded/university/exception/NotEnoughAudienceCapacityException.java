package com.foxminded.university.exception;

public class NotEnoughAudienceCapacityException extends RuntimeException {

    public NotEnoughAudienceCapacityException(String errorMessage) {
        super(errorMessage);
    }
}
