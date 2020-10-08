package com.foxminded.university.editor;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;

import java.beans.PropertyEditorSupport;

public class TeacherEditor extends PropertyEditorSupport {

    private final TeacherDao teacherDao;

    public TeacherEditor(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Teacher teacher = teacherDao.getById(parsedId).get();
        setValue(teacher);
    }
}
