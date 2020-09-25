package com.foxminded.university.exception;

public class EntityNotUpdatedException extends RuntimeException {

    public EntityNotUpdatedException(String errorMessage) {
        super(errorMessage);
    }
}
