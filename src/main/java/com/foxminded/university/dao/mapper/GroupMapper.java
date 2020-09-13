package com.foxminded.university.dao.mapper;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<Group> {

    private final StudentDao studentDao;

    public GroupMapper(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getInt("id"));
        group.setName(resultSet.getString("name"));
        group.setStudents(studentDao.getAllByGroupId(group.getId()));
        return group;
    }
}
