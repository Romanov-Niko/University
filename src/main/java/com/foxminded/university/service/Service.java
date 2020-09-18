package com.foxminded.university.service;

import java.util.List;

public interface Service<T> {

    T getById(int id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(int id);
}
