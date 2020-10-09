package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.exception.AudienceRoomNumberNotUniqueException;
import com.foxminded.university.exception.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AudienceService {

    private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);

    private final AudienceDao audienceDao;

    public AudienceService(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    public Optional<Audience> getById(int id) {
        return audienceDao.getById(id);
    }

    public List<Audience> getAll() {
        return audienceDao.getAll();
    }

    public void save(Audience audience) {
        logger.debug("Saving audience: {}", audience);
        verifyAudienceUnique(audience);
        audienceDao.save(audience);
    }

    public void update(Audience audience) {
        logger.debug("Updating audience by id: {}", audience);
        verifyAudienceUnique(audience);
        verifyAudiencePresent(audience.getId());
        audienceDao.update(audience);
    }

    public void delete(int id) {
        audienceDao.delete(id);
    }

    private void verifyAudiencePresent(int id) {
        audienceDao.getById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Audience with id %d is not present", id)));
    }

    private void verifyAudienceUnique(Audience audience) {
        audienceDao.getByRoomNumber(audience.getRoomNumber()).ifPresent(audienceWithSameRoomNumber -> {
            if (audience.getId() != audienceWithSameRoomNumber.getId()) {
                throw new AudienceRoomNumberNotUniqueException(String.format("Audience with room number %d already exist", audience.getRoomNumber()));
            }
        });
    }
}
