package com.foxminded.university.editor;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.service.LessonTimeService;

import java.beans.PropertyEditorSupport;

public class LessonTimeEditor extends PropertyEditorSupport {

    private LessonTimeService lessonTimeService;

    public LessonTimeEditor(LessonTimeService lessonTimeService) {
        this.lessonTimeService = lessonTimeService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        LessonTime lessonTime = lessonTimeService.getById(parsedId).get();
        setValue(lessonTime);
    }
}
