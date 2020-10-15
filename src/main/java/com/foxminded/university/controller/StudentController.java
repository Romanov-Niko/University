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
    public String redirectToSaveForm(Student studentModel) {
        return "students/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Student> student = studentService.getById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Student is not present");
        }
        return "students/edit";
    }
}
