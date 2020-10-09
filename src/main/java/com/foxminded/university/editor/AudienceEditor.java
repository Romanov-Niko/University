package com.foxminded.university.editor;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.service.AudienceService;

import java.beans.PropertyEditorSupport;

public class AudienceEditor extends PropertyEditorSupport {

    private AudienceService audienceService;

    public AudienceEditor(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Audience audience = audienceService.getById(parsedId).get();
        setValue(audience);
    }
}
