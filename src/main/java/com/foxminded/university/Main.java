package com.foxminded.university;

import com.foxminded.university.config.ApplicationConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        AudienceDao audienceDao = context.getBean(AudienceDao.class);

        System.out.println("List of audiences is:");
        for (Audience audiences : audienceDao.getAll()) {
            System.out.println(audiences.getId()+"|"+audiences.getRoomNumber()+"|"+audiences.getCapacity());
        }

        /*System.out.println("\nGet audience with ID 2");
        Audience audience = audienceDao.getById(2);
        System.out.println(audience.getId()+"|"+audience.getRoomNumber()+"|"+audience.getCapacity());

        System.out.println("\nCreating person: ");
        Audience addAudience = new Audience( 104, 100);
        audienceDao.save(addAudience);
        System.out.println(addAudience.getId()+"|"+addAudience.getRoomNumber()+"|"+addAudience.getCapacity());


        System.out.println("List of audiences is:");
        for (Audience audiences : audienceDao.getAll()) {
            System.out.println(audiences.getId()+"|"+audiences.getRoomNumber()+"|"+audiences.getCapacity());
        }

        System.out.println("\nDeleting person with ID 7");
        audienceDao.delete(7);

        System.out.println("\nUpdate person with ID 6");

        Audience audience = audienceDao.getById(6);
        audience.setRoomNumber(666);
        audienceDao.update(audience);

        System.out.println("List of audiences is:");
        for (Audience audiences : audienceDao.getAll()) {
            System.out.println(audiences.getId()+"|"+audiences.getRoomNumber()+"|"+audiences.getCapacity());
        }

        context.close();
*/
        /*AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        DayScheduleDao dayScheduleDao = context.getBean(DayScheduleDao.class);
        DaySchedule daySchedule = dayScheduleDao.getById(1);
        System.out.println(daySchedule.getId()+"|"+daySchedule.getDay());
        daySchedule.getLessons().forEach(lesson -> System.out.println(lesson.getSubject().getName()));*/

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        LessonDao lessonDao = context.getBean(LessonDao.class);
        System.out.println(lessonDao.getById(1).getSubject().getName());
        //lessons.forEach(lesson -> System.out.println(lesson.getSubject().getName()));
    }
}
