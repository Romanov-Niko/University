package com.foxminded.university.editor;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.service.SubjectService;

import java.beans.PropertyEditorSupport;

public class SubjectEditor extends PropertyEditorSupport {

    private SubjectService subjectService;

    public SubjectEditor(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Subject subject = subjectService.getById(parsedId).get();
        setValue(subject);
    }
}
