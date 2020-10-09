package com.foxminded.university.controller;

import com.foxminded.university.domain.*;
import com.foxminded.university.editor.*;
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Audience.class, new AudienceEditor(audienceService));
        binder.registerCustomEditor(LessonTime.class, new LessonTimeEditor(lessonTimeService));
        binder.registerCustomEditor(Subject.class, new SubjectEditor(subjectService));
        binder.registerCustomEditor(Teacher.class, new TeacherEditor(teacherService));
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("lessons", lessonService.getAll());
        return "lessons/lessons";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("allGroups", groupService.getAll());
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeService.getAll());
        model.addAttribute("allSubjects", subjectService.getAll());
        return "lessons/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("lesson") Lesson lesson, @RequestParam(value = "groupsOfLesson", required = false) int[] groupsOfLesson, RedirectAttributes redirectAttributes) {
        List<Group> groups = new ArrayList<>();
        if (groupsOfLesson != null) {
            for (int id : groupsOfLesson) {
                Group group = groupService.getById(id).get();
                groups.add(group);
            }
            lesson.setGroups(groups);
        }

        try {
            lessonService.save(lesson);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/lessons/new";
        }
        return "redirect:/lessons";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        lessonService.delete(id);
        return "redirect:/lessons";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Lesson> lesson = lessonService.getById(id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("allGroups", groupService.getAll());
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeService.getAll());
        model.addAttribute("allSubjects", subjectService.getAll());
        return "lessons/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("lesson") Lesson lesson, @RequestParam(value = "groupsOfLesson", required = false) int[] groupsOfLesson, RedirectAttributes redirectAttributes) {
        List<Group> groups = new ArrayList<>();
        if (groupsOfLesson != null) {
            for (int id : groupsOfLesson) {
                Group group = groupService.getById(id).get();
                groups.add(group);
            }
            lesson.setGroups(groups);
        }
        try {
            lessonService.update(lesson);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/lessons/edit/" + lesson.getId();
        }
        return "redirect:/lessons";
    }

    @GetMapping("groups/{id}")
    public String showSubjects(@PathVariable("id") int id, Model model) {
        Optional<Lesson> lesson = lessonService.getById(id);
        model.addAttribute("groups", lesson.get().getGroups());
        return "lessons/groups";
    }
}
