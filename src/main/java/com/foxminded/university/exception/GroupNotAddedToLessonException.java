package com.foxminded.university.exception;

public class GroupNotAddedToLessonException extends RuntimeException {

    public GroupNotAddedToLessonException(String errorMessage) {
        super(errorMessage);
    }
}
