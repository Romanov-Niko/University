package com.foxminded.university.exception;

public class SubjectNotRemovedFromTeacherException extends RuntimeException {

    public SubjectNotRemovedFromTeacherException(String errorMessage) {
        super(errorMessage);
    }
}
