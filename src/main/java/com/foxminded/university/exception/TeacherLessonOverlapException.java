package com.foxminded.university.exception;

public class TeacherLessonOverlapException extends RuntimeException {

    public TeacherLessonOverlapException(String errorMessage) {
        super(errorMessage);
    }
}
