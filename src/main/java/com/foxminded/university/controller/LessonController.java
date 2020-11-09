package com.foxminded.university.controller;

import com.foxminded.university.domain.*;
import com.foxminded.university.editor.AudienceEditor;
import com.foxminded.university.editor.LessonTimeEditor;
import com.foxminded.university.editor.SubjectEditor;
import com.foxminded.university.editor.TeacherEditor;
import com.foxminded.university.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
        model.addAttribute("lessons", lessonService.findAll());
        return "lessons/lessons";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model, Lesson lessonModel) {
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        return "lessons/new";
    }

    @Transactional
    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Lesson> lesson = lessonService.findById(id);
        if (lesson.isPresent()) {
            model.addAttribute("lesson", lesson.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Lesson is not present");
        }
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        return "lessons/edit";
    }

    @GetMapping("groups/{lessonId}")
    public String showGroups(@PathVariable int lessonId, Model model) {
        Optional<Lesson> lesson = lessonService.findById(lessonId);
        lesson.ifPresent(currentLesson -> model.addAttribute("groups", currentLesson.getGroups()));
        return "lessons/groups";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("lesson") @Valid Lesson lesson, BindingResult bindingResult,
                       @RequestParam(value = "groupsOfLesson", required = false) int[] groupsOfLesson,
                       RedirectAttributes redirectAttributes, Model model) {
        setGroupToLesson(lesson, groupsOfLesson);
        if (bindingResult.hasErrors()) {
            model.addAttribute("lesson", lesson);
            model.addAttribute("allGroups", groupService.findAll());
            model.addAttribute("allTeachers", teacherService.findAll());
            model.addAttribute("allAudiences", audienceService.getAll());
            model.addAttribute("allLessonsTimes", lessonTimeService.findAll());
            model.addAttribute("allSubjects", subjectService.findAll());
            return "/lessons/new";
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
    public String delete(@PathVariable int id) {
        lessonService.delete(id);
        return "redirect:/lessons";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("lesson") @Valid Lesson lesson, BindingResult bindingResult,
                         @RequestParam(value = "groupsOfLesson", required = false) int[] groupsOfLesson,
                         RedirectAttributes redirectAttributes, Model model) {
        setGroupToLesson(lesson, groupsOfLesson);
        if (bindingResult.hasErrors()) {
            model.addAttribute("lesson", lesson);
            model.addAttribute("allGroups", groupService.findAll());
            model.addAttribute("allTeachers", teacherService.findAll());
            model.addAttribute("allAudiences", audienceService.getAll());
            model.addAttribute("allLessonsTimes", lessonTimeService.findAll());
            model.addAttribute("allSubjects", subjectService.findAll());
            return "/lessons/edit";
        }
        try {
            lessonService.update(lesson);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/lessons/edit/" + lesson.getId();
        }
        return "redirect:/lessons";
    }

    private void setGroupToLesson(@ModelAttribute("lesson") Lesson lesson, @RequestParam(value = "groupsOfLesson", required = false) int[] groupsOfLesson) {
        List<Group> groups = new ArrayList<>();
        if (groupsOfLesson != null) {
            for (int id : groupsOfLesson) {
                Optional<Group> group = groupService.findById(id);
                group.ifPresent(groups::add);
            }
            lesson.setGroups(groups);
        }
    }
}
