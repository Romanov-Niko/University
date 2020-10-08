package com.foxminded.university.editor;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

public class AudienceEditor extends PropertyEditorSupport {

    private AudienceDao audienceDao;

    public AudienceEditor(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int parsedId = Integer.parseInt(text);
        Audience audience = audienceDao.getById(parsedId).get();
        setValue(audience);
    }
}
