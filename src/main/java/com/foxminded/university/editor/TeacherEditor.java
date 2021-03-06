package com.foxminded.university.editor;

import com.foxminded.university.domain.Teacher;
import com.foxminded.university.service.TeacherService;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

public class TeacherEditor extends PropertyEditorSupport {

    private final TeacherService teacherService;

    public TeacherEditor(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Optional<Teacher> teacher = teacherService.findById(parsedId);
        teacher.ifPresent(this::setValue);
    }
}

