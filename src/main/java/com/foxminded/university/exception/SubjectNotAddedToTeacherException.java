package com.foxminded.university.exception;

public class SubjectNotAddedToTeacherException extends RuntimeException {

    public SubjectNotAddedToTeacherException(String errorMessage) {
        super(errorMessage);
    }
}
