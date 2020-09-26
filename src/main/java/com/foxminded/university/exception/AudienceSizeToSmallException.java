package com.foxminded.university.exception;

public class AudienceSizeToSmallException extends RuntimeException {

    public AudienceSizeToSmallException(String errorMessage) {
        super(errorMessage);
    }
}
