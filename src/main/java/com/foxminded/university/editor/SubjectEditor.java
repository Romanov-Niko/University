package com.foxminded.university.editor;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.service.SubjectService;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

public class SubjectEditor extends PropertyEditorSupport {

    private SubjectService subjectService;

    public SubjectEditor(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Optional<Subject> subject = subjectService.getById(parsedId);
        subject.ifPresent(this::setValue);
    }
}

