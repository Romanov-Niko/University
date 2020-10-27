package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Subject;
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
public class JdbcSubjectDao implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcSubjectDao.class);

    private static final String SQL_GET_ALL_SUBJECTS = "SELECT * FROM subjects";
    private static final String SQL_GET_ALL_SUBJECTS_BY_TEACHER_ID = "SELECT teachers_subjects.teacher_id, subjects.id, subjects.name, " +
            "subjects.credit_hours, subjects.course, subjects.specialty " +
            "FROM teachers_subjects " +
            "LEFT JOIN teachers ON teachers_subjects.teacher_id = teachers.id " +
            "LEFT JOIN subjects ON teachers_subjects.subject_id = subjects.id " +
            "WHERE teachers.id = :teacherId";
    private static final String SQL_GET_SUBJECT_BY_NAME = "SELECT * FROM subjects WHERE name = :subjectName";

    private final SessionFactory sessionFactory;

    public JdbcSubjectDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Subject> getById(int id) {
        logger.debug("Retrieving subject with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Subject.class, id));
    }

    @Override
    public List<Subject> getAll() {
        logger.debug("Retrieved all subjects");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_SUBJECTS, Subject.class).getResultList();
    }

    @Override
    public void save(Subject subject) {
        logger.debug("Saving subject");
        try {
            sessionFactory.getCurrentSession().persist(subject);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotSavedException("Subject was not saved");
        }
    }

    @Override
    public void update(Subject subject) {
        logger.debug("Updating subject with id {}", subject.getId());
        try {
            sessionFactory.getCurrentSession().merge(subject);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Subject with id %d was not updated", subject.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting subject with id {}", id);
        try {
            Optional<Subject> subjectForDelete = getById(id);
            subjectForDelete.ifPresent(subject -> sessionFactory.getCurrentSession().remove(subject));
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotDeletedException(String.format("Subject with id %d was not deleted", id));
        }
    }

    @Override
    public List<Subject> getAllByTeacherId(int id) {
        logger.debug("Retrieving subjects related to teacher with id {}", id);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_SUBJECTS_BY_TEACHER_ID, Subject.class)
                .setParameter("teacherId", id)
                .getResultList();
    }

    @Override
    public Optional<Subject> getByName(String name) {
        logger.debug("Retrieving subject with name {}", name);
        try {
            return Optional.of(sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_SUBJECT_BY_NAME, Subject.class)
                    .setParameter("subjectName", name)
                    .getSingleResult());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
