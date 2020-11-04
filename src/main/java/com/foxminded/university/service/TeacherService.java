package com.foxminded.university.service;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.repository.SubjectRepository;
import com.foxminded.university.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    public TeacherService(TeacherRepository teacherRepository, SubjectRepository subjectRepository) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
    }

    public Optional<Teacher> findById(int id) {
        return teacherRepository.findById(id);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public void save(Teacher teacher) {
        logger.debug("Saving teacher: {}", teacher);
        verifySubjectsPresent(teacher.getSubjects());
        teacherRepository.save(teacher);
    }

    public void update(Teacher teacher) {
        logger.debug("Updating teacher by id: {}", teacher);
        verifyTeacherPresent(teacher.getId());
        verifySubjectsPresent(teacher.getSubjects());
        teacherRepository.save(teacher);
    }

    public void delete(int id) {
        teacherRepository.deleteById(id);
    }

    private void verifyTeacherPresent(int id) {
        teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Teacher with id %d is not present", id)));
    }

    private void verifySubjectsPresent(List<Subject> subjects) {
        subjects.forEach(subject -> subjectRepository.findById(subject.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", subject.getId()))));
    }
}
