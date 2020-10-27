package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
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
public class JdbcStudentDao implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcStudentDao.class);

    private static final String SQL_GET_ALL_STUDENTS = "SELECT * FROM students";
    private static final String SQL_GET_ALL_STUDENTS_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = :groupId";
    private static final String SQL_GET_ALL_STUDENTS_BY_GROUP_NAME = "SELECT * FROM students " +
            "LEFT JOIN groups ON students.group_id = groups.id " +
            "WHERE groups.name = :groupName";

    private final SessionFactory sessionFactory;

    public JdbcStudentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Student> getById(int id) {
        logger.debug("Retrieving student with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Student.class, id));
    }

    @Override
    public List<Student> getAll() {
        logger.debug("Retrieved all students");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_STUDENTS, Student.class).getResultList();
    }

    @Override
    public void save(Student student) {
        logger.debug("Saving student");
        try {
            sessionFactory.getCurrentSession().persist(student);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotSavedException("Student was not saved");
        }
    }

    @Override
    public void update(Student student) {
        logger.debug("Updating student with id {}", student.getId());
        try {
            sessionFactory.getCurrentSession().merge(student);
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Student with id %d was not updated", student.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting student with id {}", id);
        try {
            Optional<Student> studentForDelete = getById(id);
            studentForDelete.ifPresent(student -> sessionFactory.getCurrentSession().remove(student));
            sessionFactory.getCurrentSession().flush();
        } catch (Exception exception) {
            throw new EntityNotDeletedException(String.format("Student with id %d was not deleted", id));
        }
    }

    @Override
    public List<Student> getAllByGroupId(int id) {
        logger.debug("Retrieving students with group id {}", id);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_STUDENTS_BY_GROUP_ID, Student.class)
                .setParameter("groupId", id)
                .getResultList();
    }

    @Override
    public List<Student> getAllByGroupName(String name) {
        logger.debug("Retrieving student with group name {}", name);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_STUDENTS_BY_GROUP_NAME, Student.class)
                .setParameter("groupName", name)
                .getResultList();
    }
}
