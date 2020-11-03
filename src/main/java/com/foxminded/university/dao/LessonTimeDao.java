package com.foxminded.university.dao;

import com.foxminded.university.domain.LessonTime;

import java.time.LocalTime;
import java.util.Optional;

public interface LessonTimeDao extends Dao<LessonTime> {

    Optional<LessonTime> getByStartAndEndTime(LocalTime start, LocalTime end);
}
