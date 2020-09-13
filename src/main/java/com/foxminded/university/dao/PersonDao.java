package com.foxminded.university.dao;

import com.foxminded.university.domain.Person;

import java.util.List;

public interface PersonDao extends Dao<Person> {

    Person getById(int id);

    List<Person> getAll();

    void save(Person person);

    void update(Person person);

    void delete(int id);
}
