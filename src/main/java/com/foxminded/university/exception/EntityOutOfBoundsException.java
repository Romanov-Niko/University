package com.foxminded.university.exception;

public class EntityOutOfBoundsException extends RuntimeException {

    public EntityOutOfBoundsException(String errorMessage) {
        super(errorMessage);
    }
}
