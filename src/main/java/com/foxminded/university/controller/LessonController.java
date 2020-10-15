package com.foxminded.university.controller;

import com.foxminded.university.domain.*;
import com.foxminded.university.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final AudienceService audienceService;
    private final LessonTimeService lessonTimeService;

    public LessonController(LessonService lessonService, GroupService groupService, TeacherService teacherService, SubjectService subjectService, AudienceService audienceService, LessonTimeService lessonTimeService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.audienceService = audienceService;
        this.lessonTimeService = lessonTimeService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("lessons", lessonService.getAll());
        return "lessons/lessons";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model, Lesson lessonModel) {
        model.addAttribute("allGroups", groupService.getAll());
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeService.getAll());
        model.addAttribute("allSubjects", subjectService.getAll());
        return "lessons/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Lesson> lesson = lessonService.getById(id);
        if (lesson.isPresent()) {
            model.addAttribute("lesson", lesson.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Lesson is not present");
        }
        model.addAttribute("allGroups", groupService.getAll());
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeService.getAll());
        model.addAttribute("allSubjects", subjectService.getAll());
        return "lessons/edit";
    }

    @GetMapping("groups/{lessonId}")
    public String showGroups(@PathVariable int lessonId, Model model) {
        Optional<Lesson> lesson = lessonService.getById(lessonId);
        lesson.ifPresent(currentLesson -> model.addAttribute("groups", currentLesson.getGroups()));
        return "lessons/groups";
    }
}
