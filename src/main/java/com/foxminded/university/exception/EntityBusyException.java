package com.foxminded.university.exception;

public class EntityBusyException extends RuntimeException {

    public EntityBusyException(String errorMessage) {
        super(errorMessage);
    }
}
