package com.foxminded.university.exception;

public class GroupNotRemovedFromLessonException extends RuntimeException {

    public GroupNotRemovedFromLessonException(String errorMessage) {
        super(errorMessage);
    }
}
