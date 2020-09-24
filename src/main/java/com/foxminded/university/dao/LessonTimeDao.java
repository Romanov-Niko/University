package com.foxminded.university.dao;

import com.foxminded.university.domain.LessonTime;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface LessonTimeDao extends Dao<LessonTime> {

    Optional<LessonTime> getByStartAndEndTime(LocalTime start, LocalTime end);
}
