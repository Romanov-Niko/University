package com.foxminded.university.controller;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.*;
import com.foxminded.university.editor.*;
import com.foxminded.university.service.LessonService;
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
    private final LessonDao lessonDao;
    private final GroupDao groupDao;
    private final TeacherDao teacherDao;
    private final SubjectDao subjectDao;
    private final AudienceDao audienceDao;
    private final LessonTimeDao lessonTimeDao;

    public LessonController(LessonService lessonService, LessonDao lessonDao, GroupDao groupDao, TeacherDao teacherDao, SubjectDao subjectDao, AudienceDao audienceDao, LessonTimeDao lessonTimeDao) {
        this.lessonService = lessonService;
        this.lessonDao = lessonDao;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.subjectDao = subjectDao;
        this.audienceDao = audienceDao;
        this.lessonTimeDao = lessonTimeDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Audience.class, new AudienceEditor(audienceDao));
        binder.registerCustomEditor(LessonTime.class, new LessonTimeEditor(lessonTimeDao));
        binder.registerCustomEditor(Subject.class, new SubjectEditor(subjectDao));
        binder.registerCustomEditor(Teacher.class, new TeacherEditor(teacherDao));
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("lessons", lessonService.getAll());
        return "lessons/index";
    }

    @GetMapping("/new")
    public String newAudience(Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("allGroups", groupDao.getAll());
        model.addAttribute("allTeachers", teacherDao.getAll());
        model.addAttribute("allAudiences", audienceDao.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeDao.getAll());
        model.addAttribute("allSubjects", subjectDao.getAll());
        return "lessons/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("lesson") Lesson lesson, @RequestParam(value = "grou", required = false) int[] grou, RedirectAttributes redirectAttributes) {
        List<Group> groups = new ArrayList<>();
        if (grou != null) {
            for (int id : grou) {
                Group group = groupDao.getById(id).get();
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
        Optional<Lesson> lesson = lessonDao.getById(id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("allGroups", groupDao.getAll());
        model.addAttribute("allTeachers", teacherDao.getAll());
        model.addAttribute("allAudiences", audienceDao.getAll());
        model.addAttribute("allLessonsTimes", lessonTimeDao.getAll());
        model.addAttribute("allSubjects", subjectDao.getAll());
        return "lessons/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("lesson") Lesson lesson, @RequestParam(value = "grou", required = false) int[] subj, RedirectAttributes redirectAttributes) {
        List<Group> groups = new ArrayList<>();
        if (subj != null) {
            for (int id : subj) {
                Group group = groupDao.getById(id).get();
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
        Optional<Lesson> lesson = lessonDao.getById(id);
        model.addAttribute("groups", lesson.get().getGroups());
        return "lessons/groups";
    }
}
