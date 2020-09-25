package com.foxminded.university.exception;

public class EntityNotUniqueException  extends RuntimeException {

    public EntityNotUniqueException(String errorMessage) {
        super(errorMessage);
    }
}