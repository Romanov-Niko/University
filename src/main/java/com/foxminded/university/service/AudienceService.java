package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudienceService {

    private final AudienceDao audienceDao;

    public AudienceService(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    public List<Audience> getAll() {
        return audienceDao.getAll();
    }

    public void save(Audience audience) {
        if (isAudienceUnique(audience.getRoomNumber())) {
            audienceDao.save(audience);
        }
    }

    public void update(Audience audience) {
        if (isAudiencePresent(audience.getId())) {
            audienceDao.update(audience);
        }
    }

    public void delete(int id) {
        audienceDao.delete(id);
    }

    private boolean isAudiencePresent(int id) {
        return audienceDao.getById(id).isPresent();
    }

    private boolean isAudienceUnique(int roomNumber) {
        return !audienceDao.getByRoomNumber(roomNumber).isPresent();
    }
}
