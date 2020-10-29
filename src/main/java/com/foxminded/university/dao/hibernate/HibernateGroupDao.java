package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Group;
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
public class HibernateGroupDao implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateGroupDao.class);

    private static final String SQL_GET_ALL_GROUPS = "SELECT * FROM groups";
    private static final String SQL_GET_ALL_GROUPS_BY_LESSON_ID = "SELECT lessons_groups.lesson_id, groups.id, groups.name " +
            "FROM lessons_groups " +
            "LEFT JOIN lessons ON lessons_groups.lesson_id = lessons.id " +
            "LEFT JOIN groups ON lessons_groups.group_id = groups.id " +
            "WHERE lessons.id = :lessonId";
    private static final String SQL_GET_GROUP_BY_NAME = "SELECT * FROM groups WHERE name = :groupName";

    private final SessionFactory sessionFactory;

    public HibernateGroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Group> getById(int id) {
        logger.debug("Retrieving group with id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Group.class, id));
    }

    @Override
    public List<Group> getAll() {
        logger.debug("Retrieved all groups");
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_GROUPS, Group.class).getResultList();
    }

    @Override
    public void save(Group group) {
        logger.debug("Saving group");
        try {
            sessionFactory.getCurrentSession().persist(group);
        } catch (Exception exception) {
            throw new EntityNotSavedException("Group was not saved");
        }
    }

    @Override
    public void update(Group group) {
        logger.debug("Updating group with id {}", group.getId());
        try {
            sessionFactory.getCurrentSession().merge(group);
        } catch (Exception exception) {
            throw new EntityNotUpdatedException(String.format("Group with id %d was not updated", group.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting group with id {}", id);
        try {
            Optional<Group> groupForDelete = getById(id);
            groupForDelete.ifPresent(group -> sessionFactory.getCurrentSession().remove(group));
        } catch (Exception exception) {
            throw new EntityNotDeletedException(String.format("Group with id %d was not deleted", id));
        }
    }

    @Override
    public List<Group> getAllByLessonId(int id) {
        logger.debug("Retrieving groups related to lesson with id {}", id);
        return sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_ALL_GROUPS_BY_LESSON_ID, Group.class)
                .setParameter("lessonId", id)
                .getResultList();
    }

    @Override
    public Optional<Group> getByName(String name) {
        logger.debug("Retrieving group with name {}", name);
        try {
            return Optional.of(sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_GROUP_BY_NAME, Group.class)
                    .setParameter("groupName", name)
                    .getSingleResult());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
