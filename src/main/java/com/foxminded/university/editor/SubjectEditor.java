package com.foxminded.university.editor;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.domain.Subject;

import java.beans.PropertyEditorSupport;

public class SubjectEditor extends PropertyEditorSupport {

    private SubjectDao subjectDao;

    public SubjectEditor(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Subject subject = subjectDao.getById(parsedId).get();
        setValue(subject);
    }
}
