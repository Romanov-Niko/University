package com.foxminded.university.exception;

public class EntityNotDeletedException extends RuntimeException {

    public EntityNotDeletedException(String errorMessage) {
        super(errorMessage);
    }
}
