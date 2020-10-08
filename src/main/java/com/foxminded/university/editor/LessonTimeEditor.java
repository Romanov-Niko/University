package com.foxminded.university.editor;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.LessonTime;

import java.beans.PropertyEditorSupport;

public class LessonTimeEditor extends PropertyEditorSupport {

    private LessonTimeDao lessonTimeDao;

    public LessonTimeEditor(LessonTimeDao lessonTimeDao) {
        this.lessonTimeDao = lessonTimeDao;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        LessonTime lessonTime = lessonTimeDao.getById(parsedId).get();
        setValue(lessonTime);
    }
}
