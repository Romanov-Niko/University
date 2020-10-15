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
    public String redirectToSaveForm(Model model, Teacher teacherModel) {
        model.addAttribute("allSubjects", subjectService.getAll());
        return "teachers/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Teacher> teacher = teacherService.getById(id);
        if (teacher.isPresent()) {
            model.addAttribute("teacher", teacher.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Teacher is not present");
        }
        model.addAttribute("allSubjects", subjectService.getAll());
        return "teachers/edit";
    }

    @GetMapping("subjects/{teacherId}")
    public String showSubjects(@PathVariable int teacherId, Model model) {
        Optional<Teacher> teacher = teacherService.getById(teacherId);
        teacher.ifPresent(currentTeacher -> model.addAttribute("subjects", currentTeacher.getSubjects()));
        return "teachers/subjects";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("teacher") Teacher teacher, @RequestParam(value = "subjectsOfTeacher" , required = false) int[] subjectsOfTeacher , RedirectAttributes redirectAttributes) {
        List<Subject> subjects = new ArrayList<>();
        if(subjectsOfTeacher != null) {
            for (int id: subjectsOfTeacher){
                Optional<Subject> subject = subjectService.getById(id);
                subject.ifPresent(subjects::add);
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
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher, @RequestParam(value = "subjectsOfTeacher" , required = false) int[] subjectsOfTeacher, RedirectAttributes redirectAttributes) {
        List<Subject> subjects = new ArrayList<>();
        if(subjectsOfTeacher != null) {
            for (int id: subjectsOfTeacher){
                Optional<Subject> subject = subjectService.getById(id);
                subject.ifPresent(subjects::add);
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
