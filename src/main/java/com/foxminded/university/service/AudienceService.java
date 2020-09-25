package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudienceService {

    private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);

    private final AudienceDao audienceDao;

    public AudienceService(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    public List<Audience> getAll() {
        return audienceDao.getAll();
    }

    public void save(Audience audience) {
        logger.debug("Check consistency of audience before saving");
        if (isAudienceUnique(audience.getRoomNumber())) {
            audienceDao.save(audience);
        }
    }

    public void update(Audience audience) {
        logger.debug("Check consistency of audience with id {} before updating", audience.getId());
        if (isAudiencePresent(audience.getId())) {
            audienceDao.update(audience);
        }
    }

    public void delete(int id) {
        audienceDao.delete(id);
    }

    private boolean isAudiencePresent(int id) {
        if (audienceDao.getById(id).isPresent()) {
            logger.debug("Audience is present");
            return true;
        } else {
            throw new EntityNotFoundException(String.format("Audience with id %d is not present", id));
        }
    }

    private boolean isAudienceUnique(int roomNumber) {
        if(!audienceDao.getByRoomNumber(roomNumber).isPresent()) {
            logger.debug("Audience is unique");
            return true;
        } else {
            throw new EntityNotUniqueException(String.format("Audience with room number %d already exist", roomNumber));
        }
    }
}
