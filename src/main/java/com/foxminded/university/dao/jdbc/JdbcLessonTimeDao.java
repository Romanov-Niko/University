package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class JdbcLessonTimeDao implements LessonTimeDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLessonTimeDao.class);

    private static final String SQL_GET_ALL_LESSONS_TIMES = "SELECT * FROM lessons_times";
    private static final String SQL_GET_LESSON_TIME_BY_START_AND_END_TIME = "SELECT * FROM lessons_times WHERE begin_time = :start AND end_time = :end";

    private final SessionFactory sessionFactory;

    public JdbcLessonTimeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<LessonTime> getById(int id) {
        logger.debug("Retrieving lesson time with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(LessonTime.class, id));
    }

    @Override
    public List<LessonTime> getAll() {
        logger.debug("Retrieved all audiences");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_LESSONS_TIMES, LessonTime.class).getResultList();
    }

    @Override
    public void save(LessonTime lessonTime) {
        logger.debug("Saving lesson");
        try {
            sessionFactory.getCurrentSession().persist(lessonTime);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotSavedException("Lesson time was not saved");
        }
    }

    @Override
    public void update(LessonTime lessonTime) {
        logger.debug("Updating lesson time with id {}", lessonTime.getId());
        try {
            sessionFactory.getCurrentSession().merge(lessonTime);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Lesson time with id %d was not updated", lessonTime.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting lesson time with id {}", id);
        try {
            Optional<LessonTime> lessonTimeForDelete = getById(id);
            lessonTimeForDelete.ifPresent(lessonTime -> sessionFactory.getCurrentSession().remove(lessonTime));
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotDeletedException(String.format("Lesson time with id %d was not deleted", id));
        }
    }

    @Override
    public Optional<LessonTime> getByStartAndEndTime(LocalTime start, LocalTime end) {
        logger.debug("Retrieving lesson time with start {} and end {}", start, end);
        try {
            return Optional.of(sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_LESSON_TIME_BY_START_AND_END_TIME, LessonTime.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
