package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class JdbcLessonDao implements LessonDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLessonDao.class);

    private static final String SQL_GET_ALL_LESSONS = "SELECT * FROM lessons";
    private static final String SQL_GET_ALL_LESSONS_BY_DAY = "SELECT * FROM lessons WHERE date = :date";
    private static final String SQL_GET_ALL_BY_TEACHER_ID_DATE_AND_TIME_ID = "SELECT * FROM lessons " +
            "WHERE teacher_id = :teacherId AND date = :date AND lesson_time_id = :lessonTimeId";
    private static final String SQL_GET_ALL_BY_AUDIENCE_ID_DATE_AND_TIME_ID = "SELECT * FROM lessons " +
            "WHERE audience_id = :audienceId AND date = :date AND lesson_time_id = :lessonTimeId";

    private final SessionFactory sessionFactory;

    public JdbcLessonDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Lesson> getById(int id) {
        logger.debug("Retrieving lesson with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().find(Lesson.class, id));
    }

    @Override
    public List<Lesson> getAll() {
        logger.debug("Retrieved all lessons");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_LESSONS, Lesson.class).getResultList();
    }

    @Override
    public void save(Lesson lesson) {
        logger.debug("Saving lesson");
        try {
            sessionFactory.getCurrentSession().persist(lesson);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotSavedException("Lesson was not saved");
        }
    }

    @Override
    public void update(Lesson lesson) {
        logger.debug("Updating lesson with id {}", lesson.getId());
        try {
            sessionFactory.getCurrentSession().merge(lesson);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Lesson with id %d was not updated", lesson.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting lesson with id {}", id);
        try {
            Optional<Lesson> lessonForDelete = getById(id);
            lessonForDelete.ifPresent(lesson -> sessionFactory.getCurrentSession().remove(lesson));
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotDeletedException(String.format("Lesson with id %d was not deleted", id));
        }
    }

    @Override
    public List<Lesson> getAllByDate(LocalDate date) {
        logger.debug("Retrieving lessons for date {}", date);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_LESSONS_BY_DAY, Lesson.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Lesson> getAllByTeacherIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId) {
        logger.debug("Retrieving lessons with teacher id {}, date {} and lesson time id {}", id, date, lessonTimeId);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_BY_TEACHER_ID_DATE_AND_TIME_ID, Lesson.class)
                .setParameter("teacherId", id)
                .setParameter("date", date)
                .setParameter("lessonTimeId", lessonTimeId)
                .getResultList();
    }

    @Override
    public List<Lesson> getAllByAudienceIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId) {
        logger.debug("Retrieving lessons with audience id {}, date {} and lesson time id {}", id, date, lessonTimeId);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_BY_AUDIENCE_ID_DATE_AND_TIME_ID, Lesson.class)
                .setParameter("audienceId", id)
                .setParameter("date", date)
                .setParameter("lessonTimeId", lessonTimeId)
                .getResultList();
    }
}
