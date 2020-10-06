package com.foxminded.university.controller;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
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
    private final TeacherDao teacherDao;
    private final SubjectDao subjectDao;

    public TeacherController(TeacherService teacherService, TeacherDao teacherDao, SubjectDao subjectDao) {
        this.teacherService = teacherService;
        this.teacherDao = teacherDao;
        this.subjectDao = subjectDao;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "teachers/index";
    }

    @GetMapping("/new")
    public String newAudience(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allSubjects", subjectDao.getAll());
        return "teachers/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("teacher") Teacher teacher, @RequestParam(value = "subj" , required = false) int[] subj , RedirectAttributes redirectAttributes) {
        List<Subject> subjects = new ArrayList<>();
        if(subj != null) {
            for (int id: subj){
                Subject subject = subjectDao.getById(id).get();
                subjects.add(subject);
            }
            teacher.setSubjects(subjects);
        }

        try {
            teacherService.save(teacher);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/teachers/new";
        }
        return "redirect:/teachers";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Teacher> teacher = teacherDao.getById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("allSubjects", subjectDao.getAll());
        return "teachers/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher, @RequestParam(value = "subj" , required = false) int[] subj, RedirectAttributes redirectAttributes) {
        List<Subject> subjects = new ArrayList<>();
        if(subj != null) {
            for (int id: subj){
                Subject subject = subjectDao.getById(id).get();
                subjects.add(subject);
            }
            teacher.setSubjects(subjects);
        }
        try {
            teacherService.update(teacher);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/teachers/edit/"+teacher.getId();
        }
        return "redirect:/teachers";
    }
}
