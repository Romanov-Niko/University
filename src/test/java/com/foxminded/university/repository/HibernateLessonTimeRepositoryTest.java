package com.foxminded.university.repository;

import com.foxminded.university.domain.LessonTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class HibernateLessonTimeRepositoryTest {

    @Autowired
    private LessonTimeRepository lessonTimeRepository;

    @Test
    void given8AMAnd9AM_whenFindByStartAndEndTime_thenReturnedFirstLessonTime() {
        LessonTime actualLessonTime = lessonTimeRepository.findByBeginAndEnd(LocalTime.parse("08:00:00"), LocalTime.parse("09:00:00")).orElse(null);

        assertEquals(retrievedLessonTime, actualLessonTime);
    }

    @Test
    void givenWrongTime_whenFindByStartAndEndTime_thenReturnedOptionalEmpty() {
        Optional<LessonTime> actualLessonTime = lessonTimeRepository.findByBeginAndEnd(LocalTime.parse("01:00:00"), LocalTime.parse("02:00:00"));

        assertEquals(Optional.empty(), actualLessonTime);
    }
}