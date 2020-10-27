package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Teacher;
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
public class JdbcTeacherDao implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTeacherDao.class);

    private static final String SQL_GET_ALL_TEACHERS = "SELECT * FROM teachers";

    private final SessionFactory sessionFactory;

    public JdbcTeacherDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Teacher> getById(int id) {
        logger.debug("Retrieving teacher with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Teacher.class, id));
    }

    @Override
    public List<Teacher> getAll() {
        logger.debug("Retrieved all teachers");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_TEACHERS, Teacher.class).getResultList();
    }

    @Override
    public void save(Teacher teacher) {
        logger.debug("Saving teacher");
        try {
            sessionFactory.getCurrentSession().persist(teacher);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotSavedException("Teacher was not saved");
        }
    }

    @Override
    public void update(Teacher teacher) {
        logger.error("Updating teacher with id {}", teacher.getId());
        try {
            sessionFactory.getCurrentSession().merge(teacher);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Teacher with id %d was not updated", teacher.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting teacher with id {}", id);
        try {
            Optional<Teacher> teacherForDelete = getById(id);
            teacherForDelete.ifPresent(teacher -> sessionFactory.getCurrentSession().remove(teacher));
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotDeletedException(String.format("Teacher with id %d was not deleted", id));
        }
    }
}
