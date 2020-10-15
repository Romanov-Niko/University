package com.foxminded.university.editor;

import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.service.LessonTimeService;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

public class LessonTimeEditor extends PropertyEditorSupport {

    private LessonTimeService lessonTimeService;

    public LessonTimeEditor(LessonTimeService lessonTimeService) {
        this.lessonTimeService = lessonTimeService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Optional<LessonTime> lessonTime = lessonTimeService.getById(parsedId);
        lessonTime.ifPresent(this::setValue);
    }
}
