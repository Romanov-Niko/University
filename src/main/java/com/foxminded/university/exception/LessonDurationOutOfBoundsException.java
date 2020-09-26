package com.foxminded.university.exception;

public class LessonDurationOutOfBoundsException extends RuntimeException {

    public LessonDurationOutOfBoundsException(String errorMessage) {
        super(errorMessage);
    }
}
