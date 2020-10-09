package com.foxminded.university.controller;

import com.foxminded.university.domain.Student;
import com.foxminded.university.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "students/students";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        try {
            studentService.save(student);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/students/new";
        }
        return "redirect:/students";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        studentService.delete(id);
        return "redirect:/students";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Student> student = studentService.getById(id);
        model.addAttribute("student", student);
        return "students/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        try {
            studentService.update(student);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/students/edit/"+student.getId();
        }
        return "redirect:/students";
    }
}
