package com.foxminded.university.service;

import com.foxminded.university.repository.AudienceRepository;
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

    private final AudienceRepository audienceRepository;

    public AudienceService(AudienceRepository audienceRepository) {
        this.audienceRepository = audienceRepository;
    }

    public Optional<Audience> getById(int id) {
        return audienceRepository.findById(id);
    }

    public List<Audience> getAll() {
        return (List<Audience>) audienceRepository.findAll();
    }

    public void save(Audience audience) {
        logger.debug("Saving audience: {}", audience);
        verifyAudienceUnique(audience);
        audienceRepository.save(audience);
    }

    public void update(Audience audience) {
        logger.debug("Updating audience by id: {}", audience);
        verifyAudienceUnique(audience);
        verifyAudiencePresent(audience.getId());
        audienceRepository.save(audience);
    }

    public void delete(int id) {
        audienceRepository.deleteById(id);
    }

    private void verifyAudiencePresent(int id) {
        audienceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Audience with id %d is not present", id)));
    }

    private void verifyAudienceUnique(Audience audience) {
        audienceRepository.findByRoomNumber(audience.getRoomNumber()).ifPresent(audienceWithSameRoomNumber -> {
            if (audience.getId() != audienceWithSameRoomNumber.getId()) {
                throw new AudienceRoomNumberNotUniqueException(String.format("Audience with room number %d already exist", audience.getRoomNumber()));
            }
        });
    }
}
