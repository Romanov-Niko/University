package com.foxminded.university.controller;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;

    public TeacherController(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "teachers/teachers";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allSubjects", subjectService.getAll());
        return "teachers/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Teacher> teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("allSubjects", subjectService.getAll());
        return "teachers/edit";
    }

    @GetMapping("subjects/{id}")
    public String showSubjects(@PathVariable("id") int id, Model model) {
        Optional<Teacher> teacher = teacherService.getById(id);
        model.addAttribute("subjects", teacher.get().getSubjects());
        return "teachers/subjects";
    }
}
