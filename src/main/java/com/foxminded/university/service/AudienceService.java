package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AudienceService implements Service<Audience>{

    private final AudienceDao audienceDao;

    @Autowired
    public AudienceService(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    @Override
    public Audience getById(int id) {
        return audienceDao.getById(id);
    }

    @Override
    public List<Audience> getAll() {
        return audienceDao.getAll();
    }

    @Override
    public void save(Audience audience) {
        audienceDao.save(audience);
    }

    @Override
    public void update(Audience audience) {
        audienceDao.update(audience);
    }

    @Override
    public void delete(int id) {
        audienceDao.delete(id);
    }
}
