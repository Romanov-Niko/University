package com.foxminded.university.exception;

public class LessonTimeNotUniqueException extends RuntimeException {

    public LessonTimeNotUniqueException(String errorMessage) {
        super(errorMessage);
    }
}
