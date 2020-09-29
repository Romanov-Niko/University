package com.foxminded.university.exception;

public class AudienceLessonOverlapException extends RuntimeException {

    public AudienceLessonOverlapException(String errorMessage) {
        super(errorMessage);
    }
}
