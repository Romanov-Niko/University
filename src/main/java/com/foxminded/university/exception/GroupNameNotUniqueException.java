package com.foxminded.university.exception;

public class GroupNameNotUniqueException extends RuntimeException {

    public GroupNameNotUniqueException(String errorMessage) {
        super(errorMessage);
    }
}
