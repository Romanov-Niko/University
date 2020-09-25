package com.foxminded.university.exception;

public class EntityNotSavedException extends RuntimeException {

    public EntityNotSavedException(String errorMessage) {
        super(errorMessage);
    }
}
