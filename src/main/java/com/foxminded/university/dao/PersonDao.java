package com.foxminded.university.dao;

import com.foxminded.university.domain.Person;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PersonDao extends Dao<Person> {

    @Override
    Person getById(int id);

    @Override
    List<Person> getAll();

    @Override
    void save(Person person);

    @Override
    void update(Person person);

    @Override
    void delete(int id);
}
