package com.foxminded.university.exception;

public class GraduationIsAfterAdmissionException extends RuntimeException {

    public GraduationIsAfterAdmissionException(String errorMessage) {
        super(errorMessage);
    }
}
