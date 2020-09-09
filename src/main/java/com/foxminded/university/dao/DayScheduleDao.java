package com.foxminded.university.dao;

import com.foxminded.university.domain.DaySchedule;
import org.springframework.stereotype.Component;

import java.util.List;

public interface DayScheduleDao extends Dao<DaySchedule> {

    @Override
    DaySchedule getById(int id);

    @Override
    List<DaySchedule> getAll();

    @Override
    void save(DaySchedule daySchedule);

    @Override
    void update(DaySchedule daySchedule);

    @Override
    void delete(int id);
}
