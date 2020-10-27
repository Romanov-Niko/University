package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class JdbcAudienceDao implements AudienceDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcAudienceDao.class);

    private static final String SQL_GET_ALL_AUDIENCES = "SELECT * FROM audiences";
    private static final String SQL_AUDIENCE_BY_ROOM_NUMBER = "SELECT * FROM audiences WHERE room_number = :roomNumber";

    private final SessionFactory sessionFactory;

    public JdbcAudienceDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Audience> getById(int id) {
        logger.debug("Retrieving audience with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Audience.class, id));
    }

    @Override
    public List<Audience> getAll() {
        logger.debug("Retrieved all audiences");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_AUDIENCES, Audience.class).getResultList();
    }

    @Override
    public void save(Audience audience) {
        logger.debug("Saving audience");
        try {
            sessionFactory.getCurrentSession().persist(audience);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotSavedException("Audience was not saved");
        }
    }

    @Override
    public void update(Audience audience) {
        logger.debug("Updating audience with id {}", audience.getId());
        try {
            sessionFactory.getCurrentSession().update(audience);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Audience with id %d was not updated", audience.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting audience with id {}", id);
        try {
            Optional<Audience> audienceForDelete = getById(id);
            audienceForDelete.ifPresent(audience -> sessionFactory.getCurrentSession().remove(audience));
            sessionFactory.getCurrentSession().flush();
        } catch(Exception exception) {
            throw new EntityNotDeletedException(String.format("Audience with id %d was not deleted", id));
        }
    }

    @Override
    public Optional<Audience> getByRoomNumber(int roomNumber) {
        logger.debug("Retrieving audience with room number {}", roomNumber);
        try {
            return Optional.ofNullable(sessionFactory.getCurrentSession().createNativeQuery(SQL_AUDIENCE_BY_ROOM_NUMBER, Audience.class)
                    .setParameter("roomNumber", roomNumber)
                    .getSingleResult());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
