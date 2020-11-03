package com.foxminded.university.service;

import com.foxminded.university.repository.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final AudienceRepository audienceRepository;
    private final LessonTimeRepository lessonTimeRepository;

    public LessonService(LessonRepository lessonRepository, TeacherRepository teacherRepository, SubjectRepository subjectRepository, GroupRepository groupRepository, AudienceRepository audienceRepository, LessonTimeRepository lessonTimeRepository) {
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.audienceRepository = audienceRepository;
        this.lessonTimeRepository = lessonTimeRepository;
    }

    public Optional<Lesson> findById(int id) {
        return lessonRepository.findById(id);
    }

    public List<Lesson> findAll() {
        return (List<Lesson>) lessonRepository.findAll();
    }

    public void save(Lesson lesson) {
        logger.debug("Saving lesson: {}", lesson);
        verifyDataConsistent(lesson);
        lessonRepository.save(lesson);
    }

    public void update(Lesson lesson) {
        logger.debug("Updating lesson by id: {}", lesson);
        verifyLessonPresent(lesson.getId());
        verifyDataConsistent(lesson);
        lessonRepository.save(lesson);
    }

    public void delete(int id) {
        lessonRepository.deleteById(id);
    }

    public List<Lesson> findAllByDate(LocalDate date) {
        return lessonRepository.findAllByDate(date);
    }

    private void verifyLessonPresent(int id) {
        lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson with id %d is not present", id)));
    }

    private void verifyTeacherPresent(int id) {
        teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Teacher with id %d is not present", id)));
    }

    private void verifySubjectPresent(int id) {
        subjectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", id)));
    }

    private void verifyGroupsPresent(List<Group> groups) {
        groups.forEach(group -> groupRepository.findById(group.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", group.getId()))));
    }

    private void verifyAudiencePresent(int id) {
        audienceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Audience with id %d is not present", id)));
    }

    private void verifyLessonTimePresent(int id) {
        lessonTimeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson time with id %d is not present", id)));
    }

    private void verifyAudienceHasEnoughCapacity(Lesson lesson) {
        int numberOfStudents = lesson.getGroups().stream().mapToInt(group -> group.getStudents().size()).sum();
        if (lesson.getAudience().getCapacity() < numberOfStudents) {
            throw new NotEnoughAudienceCapacityException(String.format("Audience with id %d has capacity %d when need %d", lesson.getAudience().getId(),
                    lesson.getAudience().getCapacity(), numberOfStudents));
        }
    }

    private void verifyTeacherHasEnoughKnowledges(Lesson lesson) {
        if (!lesson.getTeacher().getSubjects().contains(lesson.getSubject())) {
            throw new TeacherNotEnoughKnowledgesException(String.format("Teacher with id %d can not teach %s", lesson.getTeacher().getId(), lesson.getSubject().getName()));
        }
    }

    private void verifyTeacherFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonRepository.findAllByTeacherIdDateAndLessonTimeId(currentLesson.getTeacher().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new TeacherLessonOverlapException(String.format("Teacher with id %d is busy", currentLesson.getTeacher().getId()));
            }
        }
    }

    private void verifyAudienceFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonRepository.findAllByAudienceIdDateAndLessonTimeId(currentLesson.getAudience().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new AudienceLessonOverlapException(String.format("Audience with id %d is busy", currentLesson.getAudience().getId()));
            }
        }
    }

    private void verifyDataConsistent(Lesson lesson) {
        verifySubjectPresent(lesson.getSubject().getId());
        verifyTeacherPresent(lesson.getTeacher().getId());
        verifyGroupsPresent(lesson.getGroups());
        verifyAudiencePresent(lesson.getAudience().getId());
        verifyLessonTimePresent(lesson.getLessonTime().getId());
        verifyAudienceHasEnoughCapacity(lesson);
        verifyTeacherHasEnoughKnowledges(lesson);
        verifyTeacherFree(lesson);
        verifyAudienceFree(lesson);
    }
}
