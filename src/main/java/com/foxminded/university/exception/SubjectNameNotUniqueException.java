package com.foxminded.university.exception;

public class SubjectNameNotUniqueException extends RuntimeException {

    public SubjectNameNotUniqueException(String errorMessage) {
        super(errorMessage);
    }
}
