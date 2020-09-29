package com.foxminded.university.exception;

public class CourseNumberOutOfBoundsException extends RuntimeException {

    public CourseNumberOutOfBoundsException(String errorMessage) {
        super(errorMessage);
    }
}
