package com.foxminded.university.dao;

import com.foxminded.university.domain.DaySchedule;

import java.util.List;

public interface DayScheduleDao extends Dao<DaySchedule> {
    
    DaySchedule getById(int id);

    List<DaySchedule> getAll();

    void save(DaySchedule daySchedule);

    void update(DaySchedule daySchedule);

    void delete(int id);
}
